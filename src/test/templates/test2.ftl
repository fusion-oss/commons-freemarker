<#list PurchaseOrder as po>
    <PurchaseOrder PurchaseOrderNumber="${po._PurchaseOrderNumber}">
        <CustomerName>${po.CustomerName}</CustomerName>
        <CustomerAddress>${po.CustomerAddress}</CustomerAddress>
        <DeliveryNotes>${po.DeliveryNotes}</DeliveryNotes>
    </PurchaseOrder>
</#list>