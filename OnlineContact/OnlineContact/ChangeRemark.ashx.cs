using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace OnlineContact
{
    /// <summary>
    /// ChangeRemark 的摘要说明
    /// </summary>
    public class ChangeRemark : IHttpHandler
    {

        public void ProcessRequest(HttpContext context)
        {
            context.Response.ContentType = "text/plain";
            String UserName = context.Request["UserName"];
            String remark = context.Request["Remark"];
            MySqlHelper helper = new MySqlHelper();
            if (helper.getMySqlCom("UPDATE User Set Remark='"+remark+"' where UserName='" + UserName+"'") > 0)
            {
                context.Response.Write("OK");
            }
            else
            {
                context.Response.Write("Error");
            }
        }

        public bool IsReusable
        {
            get
            {
                return false;
            }
        }
    }
}