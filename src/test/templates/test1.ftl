<#assign document = document>
{
  "PurchaseOrder": [
    {
      "CustomerName": "${document.valueOf("//PurchaseOrder/CustomerName")}",
      "CustomerAddress": "${document.valueOf("//PurchaseOrder/CustomerAddress")}",
      "DeliveryNotes": "${document.valueOf("//PurchaseOrder/DeliveryNotes")}",
      "_PurchaseOrderNumber": "${document.valueOf("//PurchaseOrder/@PurchaseOrderNumber")}"
    }
  ]
}