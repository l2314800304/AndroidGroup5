using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace OnlineContact
{
    /// <summary>
    /// Delete 的摘要说明
    /// </summary>
    public class Delete : IHttpHandler
    {

        public void ProcessRequest(HttpContext context)
        {
            context.Response.ContentType = "text/plain";
            int Contact_ID = Convert.ToInt32(context.Request["Contact_ID"]);
            MySqlHelper helper = new MySqlHelper();
            helper.getMySqlCom("DELETE FROM Contact_Info where Contact_ID=" + Contact_ID);
            if (helper.getMySqlCom("DELETE FROM Contact where ID=" + Contact_ID) > 0)
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