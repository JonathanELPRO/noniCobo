package com.calyrsoft.ucbp1.features.reservation.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calyrsoft.ucbp1.features.reservation.domain.model.Reservation
import com.calyrsoft.ucbp1.features.reservation.domain.usecase.CreateReservationUseCase
import com.calyrsoft.ucbp1.features.reservation.domain.usecase.GetReservationsByUserUseCase
import com.calyrsoft.ucbp1.features.reservation.domain.usecase.RecordAdvancePaymentUseCase
import com.calyrsoft.ucbp1.features.reservation.domain.usecase.RecordRemainingPaymentUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ReservationUiState(
    val reservations: List<Reservation> = emptyList(),
    val isLoading: Boolean = false,
    val message: String? = null
)

class ReservationViewModel(
    private val createReservation: CreateReservationUseCase,
    private val getReservationsByUser: GetReservationsByUserUseCase,
    private val recordAdvancePayment: RecordAdvancePaymentUseCase,
    private val recordRemainingPayment: RecordRemainingPaymentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReservationUiState())
    val uiState: StateFlow<ReservationUiState> get() = _uiState

    fun observeReservations(userId: Long) {
        viewModelScope.launch {
            getReservationsByUser(userId).collect { list ->
                _uiState.value = _uiState.value.copy(reservations = list, isLoading = false)
            }
        }
    }

    fun create(reservation: Reservation) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                createReservation(reservation)
                _uiState.value = _uiState.value.copy(isLoading = false, message = "Reserva creada correctamente.")
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, message = e.message)
            }
        }
    }

    fun payAdvance(reservationId: Long, amount: Double) {
        viewModelScope.launch { recordAdvancePayment(reservationId, amount) }
    }

    fun payRemaining(reservationId: Long, amount: Double) {
        viewModelScope.launch { recordRemainingPayment(reservationId, amount) }
    }
}
