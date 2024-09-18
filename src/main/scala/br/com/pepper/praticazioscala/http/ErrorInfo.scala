package br.com.pepper.praticazioscala.http

enum ErrorInfo extends Serializable:

  case NotFound(msg: String)

  case BadRequest(msg: String, errors: List[String] = List.empty)

  case InternalServerError(msg: String)
