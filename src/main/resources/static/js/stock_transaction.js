$(document).ready(function() {
    // Initialize DataTable
    const table = $('#transactionTable').DataTable({
        processing: true,
        serverSide: true,
        ajax: "/api/stock/transactions",
        columns: [
            { 
                data: "product.name",
                render: function(data, type, row) {
                    return `<div><strong>${data}</strong><br><small class="text-muted">${row.reason || 'No notes'}</small></div>`;
                }
            },
            { 
                data: "type",
                render: function(data) {
                    return data === 'STOCK_IN' ? '<small class="text-muted">In</small>' : '<small class="text-muted">Out</small>';
                }
            },
            { 
                data: "quantity",
                render: function(data, type, row) {
                    const color = row.type === 'STOCK_IN' ? 'text-success' : 'text-danger';
                    const icon = row.type === 'STOCK_IN' ? 'bi-plus-circle' : 'bi-dash-circle';
                    const prefix = row.type === 'STOCK_IN' ? '+' : '-';
                    return `<span class="badge bg-light ${color} border"><i class="bi ${icon}"></i> ${prefix}${data}</span>`;
                }
            },
            { 
                data: "transactionDate",
                render: function(data) { return new Date(data).toLocaleDateString(); }
            }
        ],
        dom: 'rtp' // Simplified view: Table and Pagination only
    });

    // Dynamic UI Toggle
    $('input[name="type"]').on('change', function() {
        const isOut = $(this).val() === 'STOCK_OUT';
        $('#submitBtn').text(isOut ? 'Record Stock Out' : 'Record Stock In');
        $('#submitBtn').removeClass('btn-success btn-danger').addClass(isOut ? 'btn-danger' : 'btn-success');
    });
});