package rs.ac.metropolitan.cs330_dz11.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import rs.ac.metropolitan.cs330_dz11.common.UserItem
import rs.ac.metropolitan.cs330_dz11.repository.Repository

class AppViewModel:ViewModel() {
    lateinit var navController: NavHostController
    val repository = Repository()
    var granted = mutableStateOf(false)

    private val _userz = MutableLiveData<List<UserItem>>()
    val userz: LiveData<List<UserItem>>
        get() = _userz

    // Komunikacija sa repozitorijumom
    fun loadUserz() {
        GlobalScope.launch {
            repository.loadUserz()
            MainScope().launch {
                repository.userzFlow.collect {
                    _userz.value = it
                }
            }
        }
    }

    fun getUser(id: String): UserItem? {
        return _userz.value?.find { it.id == id }
    }
    fun deleteUser(id: String) {
        GlobalScope.launch {
            repository.deleteUser(id)
        }
        goBack()
    }

    fun submitUser(user: UserItem) {
        GlobalScope.launch {
            repository.submitUser(user)
        }
    }

    // Routing methods
    fun navigateToUserDetails(id: String) {
        navController.navigate(rs.ac.metropolitan.cs330_dz11.ui.navigation.NavigationRoutes.UserDetailScreen.createRoute(id))
    }

    fun navigateToNewUser() {
        navController.navigate(rs.ac.metropolitan.cs330_dz11.ui.navigation.NavigationRoutes.NewUser.route)
    }

    fun goBack() {
        navController.popBackStack()
    }
}