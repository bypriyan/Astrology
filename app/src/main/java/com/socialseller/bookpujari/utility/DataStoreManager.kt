package com.bypriyan.bustrackingsystem.utility


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.socialseller.ceo.apiResponce.verifyOtpResponce.Permission
import com.socialseller.ceo.apiResponce.verifyOtpResponce.User
import com.socialseller.clothcrew.utility.dataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext context: Context, private val gson: Gson) {

    private val dataStore: DataStore<Preferences> = context.dataStore

    companion object {
        val BEARER_TOKEN = stringPreferencesKey("bearer_token")
        val USER_JSON = stringPreferencesKey("user_json")
        val PERMISSIONS_JSON = stringPreferencesKey("permissions_json")
        val ORG_ID = intPreferencesKey("organization_id")
        val IS_MODULE_SELECTED = intPreferencesKey("is_module_selected")
        val NAMING_CONVENTION = stringPreferencesKey("naming_convention_employee")
    }



    suspend fun saveLoginData(
        token: String,
        user: User,
        permissions: List<Permission>,
        orgId: Int,
        isModuleSelected: Int,
        namingConvention: String
    ) {
        dataStore.edit { prefs ->
            prefs[BEARER_TOKEN] = token
            prefs[USER_JSON] = gson.toJson(user)
            prefs[PERMISSIONS_JSON] = gson.toJson(permissions)
            prefs[ORG_ID] = orgId
            prefs[IS_MODULE_SELECTED] = isModuleSelected
            prefs[NAMING_CONVENTION] = namingConvention
        }
    }

    // Get Bearer Token
    val bearerTokenFlow: Flow<String?> = dataStore.data.map { it[BEARER_TOKEN] }

    // Get User
    val userFlow: Flow<User?> = dataStore.data.map {
        it[USER_JSON]?.let { json -> gson.fromJson(json, User::class.java) }
    }

    // Get Permissions
    val permissionsFlow: Flow<List<Permission>> = dataStore.data.map {
        it[PERMISSIONS_JSON]?.let { json ->
            gson.fromJson(json, object : TypeToken<List<Permission>>() {}.type)
        } ?: emptyList()
    }

    // Get Organization ID
    val orgIdFlow: Flow<Int> = dataStore.data.map { it[ORG_ID] ?: -1 }

    // Get Module Selection
    val isModuleSelectedFlow: Flow<Int> = dataStore.data.map { it[IS_MODULE_SELECTED] ?: 0 }

    // Get Naming Convention
    val namingConventionFlow: Flow<String?> = dataStore.data.map { it[NAMING_CONVENTION] }

    // Check if permission exists
    suspend fun hasPermission(permissionName: String): Boolean {
        val permissionsJson = dataStore.data.first()[PERMISSIONS_JSON] ?: return false
        val list: List<Permission> = gson.fromJson(permissionsJson, object : TypeToken<List<Permission>>() {}.type)
        return list.any { it.name == permissionName }
    }

    // Generic Save Boolean
    suspend fun putBoolean(key: String, value: Boolean) {
        val dataStoreKey = booleanPreferencesKey(key)
        dataStore.edit { it[dataStoreKey] = value }
    }

    // Generic Get Boolean
    fun getBoolean(key: String): Flow<Boolean> {
        val dataStoreKey = booleanPreferencesKey(key)
        return dataStore.data.map { it[dataStoreKey] ?: false }
    }

    // Generic Save String
    suspend fun putString(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        dataStore.edit { it[dataStoreKey] = value }
    }

    // Generic Get String
    fun getString(key: String): Flow<String?> {
        val dataStoreKey = stringPreferencesKey(key)
        return dataStore.data.map { it[dataStoreKey] }
    }

    // Remove a key
    suspend fun removeKey(key: String) {
        val dataStoreKey = stringPreferencesKey(key)
        dataStore.edit { it.remove(dataStoreKey) }
    }

    // Clear all
    suspend fun clear() {
        dataStore.edit { it.clear() }
    }
}
