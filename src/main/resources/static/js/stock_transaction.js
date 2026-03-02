$(document).ready(function () {
    // 1. Initialize DataTable with Server-side processing
    const table = $('#transactionTable').DataTable({
        processing: true,
        serverSide: true,
        searching: true,
        autoWidth: false, // Set to false to prevent 'style' calculation errors
        scrollX: true,
        ajax: "/api/stock/transactions",
        order: [[3, "desc"]],
        columns: [
            {
                data: "product.name",
                render: function (data, type, row) {
                    return `<div><div class="fw-bold">${data}</div><small class="text-muted">${row.product.sku}</small></div>`;
                }
            },
            {
                data: "type",
                render: function (data) {
                    let badgeClass = data === 'STOCK_IN' ? 'bg-success-subtle text-success' : 'bg-danger-subtle text-danger';
                    return `<span class="badge ${badgeClass} border-0 px-3">${data}</span>`;
                }
            },
            {
                data: "quantity",
                render: function (data, type, row) {
                    const color = row.type === 'STOCK_IN' ? 'text-success' : 'text-danger';
                    const prefix = row.type === 'STOCK_IN' ? '+' : '-';
                    return `<span class="${color} fw-bold">${prefix}${data}</span>`;
                }
            },
            {
                data: "transactionDate",
                render: function (data) {
                    return data ? new Date(data).toLocaleString() : 'N/A';
                }
            },
            {
                data: "user.username",
                defaultContent: "System",
                render: function (data) {
                    return `<span class="text-muted small">${data || 'System'}</span>`;
                }
            }
        ]
    });

    // 2. Handle Form Submission via AJAX
    $('#stockForm').on('submit', function (e) {
        e.preventDefault();

        const form = $(this);
        const submitBtn = $('#submitBtn');
        
        // --- STOCK OUT VALIDATION GATE ---
        const type = $('input[name="type"]:checked').val();
        const quantity = parseInt($('#quantity').val());
        
        // Extract current quantity from the dropdown text (e.g., "Coffee Bean (Qty: 100)")
        const selectedText = $("#product option:selected").text();
        const availableMatch = selectedText.match(/\(Qty: (\d+)\)/);
        const available = availableMatch ? parseInt(availableMatch[1]) : 0;

        if (type === 'STOCK_OUT' && quantity > available) {
            Swal.fire({
                icon: 'warning',
                title: 'Insufficient Stock',
                text: `You only have ${available} units available for this product.`,
                confirmButtonColor: '#2563eb'
            });
            return; // Stop the AJAX request
        }
        // ----------------------------------

        submitBtn.prop('disabled', true).html('<span class="spinner-border spinner-border-sm"></span> Processing...');

        // Dynamically set URL based on transaction type
        const targetUrl = (type === 'STOCK_OUT') ? '/api/stock/out' : '/api/stock/save';

        $.ajax({
            url: targetUrl,
            type: 'POST',
            data: form.serialize(),
            success: function (response) {
                Swal.fire({
                    icon: 'success',
                    title: 'Stock Updated',
                    text: 'Inventory record saved successfully.',
                    timer: 2500,
                    showConfirmButton: false,
                    toast: true,
                    position: 'top-end'
                });

                table.ajax.reload();
                form[0].reset();
                
                // Reset UI to default "Stock In" state
                $('#typeIn').prop('checked', true).trigger('change');
                
                // Optional: Reload page or refresh dropdowns to update the (Qty: X) labels
                setTimeout(() => location.reload(), 1500); 
            },
            error: function (xhr) {
                Swal.fire({
                    icon: 'error',
                    title: 'Update Failed',
                    text: xhr.responseText || 'Error connecting to server.'
                });
            },
            complete: function () {
                submitBtn.prop('disabled', false);
            }
        });
    });

    // 3. Dynamic UI Toggle for Stock In/Out
    $('input[name="type"]').on('change', function () {
        const isOut = $(this).val() === 'STOCK_OUT';
        const submitBtn = $('#submitBtn');

        if (isOut) {
            submitBtn.text('Record Stock Out').removeClass('btn-success').addClass('btn-danger');
        } else {
            submitBtn.text('Record Stock In').removeClass('btn-danger').addClass('btn-success');
        }
    });
});