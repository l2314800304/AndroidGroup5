using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace OnlineContact
{
    /// <summary>
    /// ChangePassword 的摘要说明
    /// </summary>
    public class ChangePassword : IHttpHandler
    {

        public void ProcessRequest(HttpContext context)
        {
            context.Response.ContentType = "text/plain";
            String UserName = context.Request["UserName"];
            String Old = context.Request["Password"];
            String Ne = context.Request["NewPassword"];
            MySqlHelper helper = new MySqlHelper();
                if (helper.getMySqlCom("UPDATE User Set Password='" + Ne + "' where UserName='" + UserName + "' And Password='"+Old+"'") > 0)
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