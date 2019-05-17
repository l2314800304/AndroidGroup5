using MySql.Data.MySqlClient;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using System.Web;

namespace OnlineContact
{
    public class MySqlHelper
    {
        public static string Constr = ConfigurationManager.ConnectionStrings["constr"].ConnectionString;

        #region  建立MySql数据库连接
        public MySqlConnection getMySqlCon()
        {
            string M_str_sqlcon = Constr;
            MySqlConnection myCon = new MySqlConnection(M_str_sqlcon);
            return myCon;
        }
        #endregion

        #region  执行MySqlCommand命令
        /// <summary>
        /// 执行MySqlCommand,返回影响的行数
        /// </summary>
        /// <param name="M_str_sqlstr">SQL语句</param>
        public int getMySqlCom(string M_str_sqlstr, params MySqlParameter[] parameters)
        {
            MySqlConnection mysqlcon = this.getMySqlCon();
            mysqlcon.Open();
            MySqlCommand mysqlcom = new MySqlCommand(M_str_sqlstr, mysqlcon);
            mysqlcom.Parameters.AddRange(parameters);
            int count = mysqlcom.ExecuteNonQuery();
            mysqlcom.Dispose();
            mysqlcon.Close();
            mysqlcon.Dispose();
            return count;
        }
        #endregion

        #region  创建MySqlDataReader对象
        /// <summary>
        /// 执行查询语句,返回MySqlDataReader
        /// </summary>
        /// <param name="M_str_sqlstr">SQL语句</param>
        /// <returns>返回MySqlDataReader对象</returns>
        public MySqlDataReader getMySqlReader(string M_str_sqlstr, params MySqlParameter[] parameters)
        {
            MySqlConnection mysqlcon = this.getMySqlCon();
            mysqlcon.Open();
            MySqlCommand mysqlcom = new MySqlCommand(M_str_sqlstr, mysqlcon);
            mysqlcom.Parameters.AddRange(parameters);
            MySqlDataReader reader = mysqlcom.ExecuteReader();
            return reader;
        }
        #endregion
    }
}