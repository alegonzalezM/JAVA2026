const TOKEN_DESARROLLO = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c3VhcmlvLnBydWViYUB0ZWNobGFiLmNvbSIsImlhdCI6MTg4NTEyMDAwMCwiZXhwIjoyMTEyMzg0MDAwLCJyb2xlcyI6WyJVU0VSIl19.zZ_4_D8g_9g2_M4_X5v_Wv_X5vXWv_X5vXWv_M4_X5v_Wv_X5vXWv";
const API_URL = '/productos'; 

// 1. Intentamos recuperar el ID del carrito activo de la sesión
let ID_CARRITO_SESION = sessionStorage.getItem('id_carrito_activo');

// 2. Declaración de funciones primero para evitar el ReferenceError
function obtenerCarritoInicial() {
    if (!ID_CARRITO_SESION) return;
    
    fetch("/carritos/" + ID_CARRITO_SESION, {
        method: 'GET',
        cache: 'no-store',
        headers: { 'Authorization': TOKEN_DESARROLLO }
    })
    .then(res => {
        if (!res.ok) throw new Error("Status: " + res.status);
        return res.json();
    })
    .then(carrito => {
        let totalItems = 0;
        const listaProductos = carrito.productos || carrito.items || [];
        listaProductos.forEach(item => {
            totalItems += item.cantidad || 1;
        });
        const badge = document.querySelector('.cart-badge');
        if (badge) badge.innerText = totalItems;
    })
    .catch(err => console.log("Aviso en carrito inicial:", err.message));
}

function cargarCatalogo() {
    fetch(API_URL, {
        method: 'GET',
        cache: 'no-store',
        headers: { 'Authorization': TOKEN_DESARROLLO }
    })
    .then(res => res.json())
    .then(productos => {
        const grid = document.getElementById('grid-productos');
        if (!grid) return; 
        grid.innerHTML = ''; 

        productos.forEach(p => {
            const card = document.createElement('div');
            card.className = 'product-card';
            let imgUrl = p.imagen || "/imagenes/producto-defecto.jpg";
            const marcaNombre = p.marca && p.marca.nombre ? p.marca.nombre : 'Genérica';

            card.innerHTML = `
                <div class="image-container"><img src="${imgUrl}"></div>
                <div class="product-title">${p.nombre}</div>
                <div class="product-brand">Marca: ${marcaNombre}</div>
                <div class="product-category">${p.categoria || 'General'}</div>
                <div class="product-price">$${p.precio.toLocaleString('es-AR', { minimumFractionDigits: 2 })}</div>
                <div class="stock-badge">Stock: ${p.stock || 0}</div>
                <button class="btn-card-cart" onclick="agregarAlCarrito(${p.id})">+ Añadir al Carrito</button>
            `;
            grid.appendChild(card);
        });
    })
    .catch(err => console.error("Error al cargar catálogo:", err));
}

function agregarAlCarrito(productoId) {
    if (!ID_CARRITO_SESION) {
        notificar("Inicializando carrito, por favor intenta de nuevo.", "error");
        return;
    }

    fetch("/carritos/" + ID_CARRITO_SESION + "/productos/" + productoId, {
        method: 'POST',
        headers: { 'Authorization': TOKEN_DESARROLLO }
    })
    .then(res => {
        if (!res.ok) throw new Error("Error: " + res.status);
        return res.json();
    })
    .then(carritoActualizado => {
        notificar("¡Producto añadido con éxito!", "success");
        
        const lista = carritoActualizado.productos || carritoActualizado.items || [];
        let total = 0;
        lista.forEach(item => total += item.cantidad || 1);
        
        const badge = document.querySelector('.cart-badge');
        if (badge) badge.innerText = total;
        if (document.getElementById('grid-productos')) cargarCatalogo(); //refresca el stock
        if (document.getElementById('tabla-carrito')) renderizarDetalleCarrito();
    })
    .catch(err => {
        console.error("Fallo:", err);
        notificar("Error al añadir producto.", "error");
    });
}

function renderizarDetalleCarrito() {
    const tbody = document.getElementById('tabla-carrito');
    if (!tbody) return; 

    fetch("/carritos/" + ID_CARRITO_SESION, {
        method: 'GET',
        cache: 'no-store',
        headers: { 'Authorization': TOKEN_DESARROLLO }
    })
    .then(res => res.json())
    .then(carrito => {
        tbody.innerHTML = '';
        let totalAcumulado = 0;
        const listaProductos = carrito.productos || carrito.items || [];

        if (listaProductos.length === 0) {
            tbody.innerHTML = `<tr><td colspan="4" style="text-align:center; padding:30px; color:#777;">Tu carrito está vacío.</td></tr>`;
            document.getElementById('precio-total').innerText = "$0,00";
            return;
        }

        listaProductos.forEach(item => {
            const prod = item.producto || item.prod || item.articulo || (item.precio ? item : null);
            const cantidad = item.cantidad || 1;
            if (!prod || !prod.precio) return;
            
            const subtotal = prod.precio * cantidad;
            totalAcumulado += subtotal;

            const fila = document.createElement('tr');
            fila.innerHTML = `
                <td>
                    <div style="font-weight:bold; font-size:1.1rem;">${prod.nombre}</div>
                    <div style="font-size:0.85rem; color:#888;">Código: ${prod.codigo || 'N/A'}</div>
                </td>
                <td>$${prod.precio.toLocaleString('es-AR', { minimumFractionDigits: 2 })}</td>
                <td style="font-weight:bold; color:#555;">${cantidad} u.</td>
                <td style="font-weight:bold;">$${subtotal.toLocaleString('es-AR', { minimumFractionDigits: 2 })}</td>
            `;
            tbody.appendChild(fila);
        });
        document.getElementById('precio-total').innerText = "$" + totalAcumulado.toLocaleString('es-AR', { minimumFractionDigits: 2 });
    })
    .catch(err => console.error("Error al renderizar:", err));
}

function vaciarCarrito() {
    if (!confirm("¿Estás seguro de que deseas vaciar todo el contenido del carrito?")) return;

    fetch("/carritos/" + ID_CARRITO_SESION + "/vaciar", {
        method: 'DELETE',
        headers: { 'Authorization': TOKEN_DESARROLLO }
    })
    .then(res => {
        if (res.ok) {
            notificar("¡Carrito vaciado con éxito!", "success");
            
            const badge = document.querySelector('.cart-badge');
            if (badge) badge.innerText = "0";
            
            const tbody = document.getElementById('tabla-carrito');
            if (tbody) {
                tbody.innerHTML = `<tr><td colspan="4" style="text-align:center; padding: 30px; color: #777;">Tu carrito está vacío.</td></tr>`;
            }
            const precioTotal = document.getElementById('precio-total');
            if (precioTotal) precioTotal.innerText = "$0,00";
            
            if (document.getElementById('grid-productos')) {
                cargarCatalogo();
            }
        } else {
            alert("El servidor rechazó la solicitud de vaciado.");
        }
    })
    .catch(err => console.error("Error al vaciar el carrito:", err));
}

function notificar(mensaje, tipo = "success") {
    let contenedor = document.getElementById('toast-container') || document.createElement('div');
    contenedor.id = 'toast-container'; document.body.appendChild(contenedor);
    const toast = document.createElement('div');
    toast.className = `toast-message ${tipo}`; toast.innerText = mensaje;
    contenedor.appendChild(toast);
    setTimeout(() => toast.classList.add('visible'), 10);
    setTimeout(() => { toast.classList.remove('visible'); setTimeout(() => toast.remove(), 300); }, 3000);
}

// 3. Flujo centralizado de inicialización de la tienda
function inicializarTienda() {
    if (ID_CARRITO_SESION) {
        obtenerCarritoInicial(); 
        if (document.getElementById('tabla-carrito')) renderizarDetalleCarrito(); 
        if (document.getElementById('grid-productos')) cargarCatalogo();
    } else {
        fetch("/carritos", {
            method: 'POST',
            headers: { 'Authorization': TOKEN_DESARROLLO }
        })
        .then(res => res.json())
        .then(nuevoCarrito => {
            ID_CARRITO_SESION = nuevoCarrito.id;
            sessionStorage.setItem('id_carrito_activo', ID_CARRITO_SESION); 
            
            obtenerCarritoInicial(); 
            if (document.getElementById('grid-productos')) cargarCatalogo();
        })
        .catch(err => console.error("Error al crear carrito dinámico:", err));
    }
}

document.addEventListener("DOMContentLoaded", () => {
    inicializarTienda();
});

window.addEventListener('pageshow', (event) => {
    if (event.persisted) {
        obtenerCarritoInicial();
        if (document.getElementById('grid-productos')) cargarCatalogo();
        if (document.getElementById('tabla-carrito')) renderizarDetalleCarrito();
    }
});
