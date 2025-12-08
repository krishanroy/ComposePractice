package com.krishan.composePractice.lowesPrep.data.dtos

import com.krishan.composePractice.lowesPrep.domain.model.Address
import com.krishan.composePractice.lowesPrep.domain.model.Company
import com.krishan.composePractice.lowesPrep.domain.model.Geo
import com.krishan.composePractice.lowesPrep.domain.model.User

data class UserDto(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val address: AddressDto,
    val phone: String,
    val website: String,
    val company: CompanyDto
)

data class CompanyDto(val name: String, val catchPhrase: String, val bs: String)
data class AddressDto(val street: String, val suite: String, val city: String, val zipcode: String, val geo: GeoDto)
data class GeoDto(val lat: String, val lng: String)

/**
 * Converts the UserDto (data layer) to a User (domain layer) object.
 */
fun UserDto.toDomain(): User {
    return User(
        id = this.id,
        name = this.name,
        username = this.username,
        email = this.email,
        address = this.address.toDomain(),
        phone = this.phone,
        website = this.website,
        company = this.company.toDomain()
    )
}

/**
 * Converts the AddressDto (data layer) to an Address (domain layer) object.
 */
fun AddressDto.toDomain(): Address {
    return Address(
        street = this.street,
        suite = this.suite,
        city = this.city,
        zipcode = this.zipcode,
        geo = this.geo.toDomain()
    )
}

/**
 * Converts the CompanyDto (data layer) to a Company (domain layer) object.
 */
fun CompanyDto.toDomain(): Company {
    return Company(
        name = this.name,
        catchPhrase = this.catchPhrase,
        bs = this.bs
    )
}

/**
 * Converts the GeoDto (data layer) to a Geo (domain layer) object.
 */
fun GeoDto.toDomain(): Geo {
    return Geo(
        lat = this.lat,
        lng = this.lng
    )
}
