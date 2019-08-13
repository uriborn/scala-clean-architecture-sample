package com.example.entities.client

import com.example.shared.ddd_base.Identifier

case class ClientId(value: Long) extends Identifier[Long]
