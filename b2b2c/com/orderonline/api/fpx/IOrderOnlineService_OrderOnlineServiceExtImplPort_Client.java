
package com.orderonline.api.fpx;

/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 2.6.2
 * 2015-10-26T15:41:20.111+08:00
 * Generated source version: 2.6.2
 * 
 */
public final class IOrderOnlineService_OrderOnlineServiceExtImplPort_Client {

    private static final QName SERVICE_NAME = new QName("entity.orderonline.api.fpx", "OrderOnlineServiceExtImplService");

    private IOrderOnlineService_OrderOnlineServiceExtImplPort_Client() {
    }

    public static void main(String args[]) throws java.lang.Exception {
        URL wsdlURL = OrderOnlineServiceExtImplService.WSDL_LOCATION;
        if (args.length > 0 && args[0] != null && !"".equals(args[0])) { 
            File wsdlFile = new File(args[0]);
            try {
                if (wsdlFile.exists()) {
                    wsdlURL = wsdlFile.toURI().toURL();
                } else {
                    wsdlURL = new URL(args[0]);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
      
        OrderOnlineServiceExtImplService ss = new OrderOnlineServiceExtImplService(wsdlURL, SERVICE_NAME);
        IOrderOnlineService port = ss.getOrderOnlineServiceExtImplPort();  
        
    /*    {
        //System.out.println("Invoking preAlertOrderService...");
        java.lang.String _preAlertOrderService_arg0 = "";
        java.util.List<java.lang.String> _preAlertOrderService_arg1 = null;
        java.util.List<com.orderonline.api.fpx.PreAlertOrderResponse> _preAlertOrderService__return = port.preAlertOrderService(_preAlertOrderService_arg0, _preAlertOrderService_arg1);
        //System.out.println("preAlertOrderService.result=" + _preAlertOrderService__return);


        }*/
        {
        //System.out.println("Invoking removeOrderService...");
        java.lang.String _removeOrderService_arg0 = "";
        java.util.List<java.lang.String> _removeOrderService_arg1 = null;
        java.util.List<com.orderonline.api.fpx.RemoveOrderResponse> _removeOrderService__return = port.removeOrderService(_removeOrderService_arg0, _removeOrderService_arg1);
        //System.out.println("removeOrderService.result=" + _removeOrderService__return);


        }
        {
        //System.out.println("Invoking findOrderService...");
        java.lang.String _findOrderService_arg0 = "";
        com.orderonline.api.fpx.FindOrderRequest _findOrderService_arg1 = null;
        com.orderonline.api.fpx.FindOrderResponse _findOrderService__return = port.findOrderService(_findOrderService_arg0, _findOrderService_arg1);
        //System.out.println("findOrderService.result=" + _findOrderService__return);


        }
       /* {
        //System.out.println("Invoking createOrderService...");
        java.lang.String _createOrderService_arg0 = "";
        java.util.List<com.orderonline.api.fpx.CreateOrderRequest> _createOrderService_arg1 = null;
        java.util.List<com.orderonline.api.fpx.CreateOrderResponse> _createOrderService__return = port.createOrderService(_createOrderService_arg0, _createOrderService_arg1);
        //System.out.println("createOrderService.result=" + _createOrderService__return);


        }*/
       /* {
        //System.out.println("Invoking createAndPreAlertOrderService...");
        java.lang.String _createAndPreAlertOrderService_arg0 = "";
        java.util.List<com.orderonline.api.fpx.CreateAndPreAlertOrderRequest> _createAndPreAlertOrderService_arg1 = null;
        java.util.List<com.orderonline.api.fpx.CreateAndPreAlertOrderResponse> _createAndPreAlertOrderService__return = port.createAndPreAlertOrderService(_createAndPreAlertOrderService_arg0, _createAndPreAlertOrderService_arg1);
        //System.out.println("createAndPreAlertOrderService.result=" + _createAndPreAlertOrderService__return);


        }*/
      /*  {
        //System.out.println("Invoking modifyOrderService...");
        java.lang.String _modifyOrderService_arg0 = "";
        java.util.List<com.orderonline.api.fpx.ModifyOrderRequest> _modifyOrderService_arg1 = null;
        java.util.List<com.orderonline.api.fpx.ModifyOrderResponse> _modifyOrderService__return = port.modifyOrderService(_modifyOrderService_arg0, _modifyOrderService_arg1);
        //System.out.println("modifyOrderService.result=" + _modifyOrderService__return);


        }*/

        System.exit(0);
    }

}