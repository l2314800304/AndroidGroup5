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

        public MySqlConnection mysqlcon= new MySqlConnection(Constr);
        public MySqlCommand mysqlcom;

        #region  执行MySqlCommand命令
        /// <summary>
        /// 执行MySqlCommand,返回影响的行数
        /// </summary>
        /// <param name="M_str_sqlstr">SQL语句</param>
        public int getMySqlCom(string M_str_sqlstr, params MySqlParameter[] parameters)
        {
            if (mysqlcon.State != System.Data.ConnectionState.Open)
                mysqlcon.Open();
            mysqlcom = new MySqlCommand(M_str_sqlstr, mysqlcon);
            mysqlcom.Parameters.AddRange(parameters);
            int count = 0;
            try
            {
                count = mysqlcom.ExecuteNonQuery();
            }
            catch (MySqlException)
            {
                count = 0;
            }
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
            if(mysqlcon.State!=System.Data.ConnectionState.Open)
                mysqlcon.Open();
            mysqlcom = new MySqlCommand(M_str_sqlstr, mysqlcon);
            mysqlcom.Parameters.AddRange(parameters);
            return mysqlcom.ExecuteReader();
        }
        #endregion
    }
}