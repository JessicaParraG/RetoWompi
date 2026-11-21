# language: es
@nequi @consulta
Característica: Consulta de transacciones con Nequi
  Como comercio afiliado a Wompi
  Quiero consultar mis transacciones Nequi
  Para conciliar los pagos recibidos

  Antecedentes:
    Dado que el comercio tiene sus llaves del ambiente sandbox

  @CP-07 @positivo @smoke
  Escenario: Consultar una transacción existente por ID
    Dado que el comercio creó una transacción de 25000 pesos al número Nequi "3991111111"
    Cuando el comercio consulta la transacción por su ID
    Entonces la consulta retorna la transacción solicitada
    Y la transacción registra el método de pago "NEQUI" con el número "3991111111"
    Y la transacción conserva el monto, la moneda y la referencia enviados

  @CP-08 @negativo
  Escenario: Utilizar una credencial privada inválida
    Dado que el comercio creó una transacción de 25000 pesos al número Nequi "3991111111"
    Cuando el comercio consulta las transacciones con esa referencia usando la llave privada "prv_stagtest_llaveInvalida123"
    Entonces la solicitud es rechazada por autenticación con el motivo "Llave no válida"
