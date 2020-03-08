package com.takaful.backend.service.freamwork

import com.takaful.backend.service.implementation.Pageable

interface MedicationsService {
     fun getAllMedications(page:Int,size:Int): Pageable<*>
}