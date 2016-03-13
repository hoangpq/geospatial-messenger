package io.spring.messenger.repository

import com.nurkiewicz.jdbcrepository.JdbcRepository
import com.nurkiewicz.jdbcrepository.RowUnmapper
import io.spring.messenger.domain.User
import org.postgis.PGgeometry
import org.postgis.Point
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
open class UserRepository : JdbcRepository<User, String>(UserRowMaper(), UserRowUnmapper(), "\"users\"") {

    class UserRowMaper : RowMapper<User> {
        override fun mapRow(rs: ResultSet, rowNum: Int)
                = User(
                    rs.getString("user_name"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    (rs.getObject("location") as PGgeometry?)?.geometry as Point?)
    }

    class UserRowUnmapper() : RowUnmapper<User> {
        override fun mapColumns(user: User): Map<String, Any>  {
            val map = mutableMapOf<String, Any>(
                    Pair("user_name", user.userName),
                    Pair("first_name", user.firstName),
                    Pair("last_name", user.lastName))
            if (user.location != null) {
                user.location!!.srid = 4326
                map["location"] = PGgeometry(user.location)
            }
            return map;
        }
    }

}
