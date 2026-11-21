# language: es
@nequi @creacion
Característica: Creación de transacciones con Nequi
  Como comercio afiliado a Wompi
  Quiero crear transacciones de pago con Nequi
  Para cobrar a mis clientes desde su billetera móvil

  Antecedentes:
    Dado que el comercio tiene sus llaves del ambiente sandbox

  @CP-01 @positivo @smoke
  Escenario: Crear transacción Nequi con datos válidos
    Cuando el comercio crea una transacción de 25000 pesos al número Nequi "3991111111"
    Entonces la transacción se crea en estado "PENDING"
    Y Wompi asigna un identificador a la transacción
    Y la transacción registra el método de pago "NEQUI" con el número "3991111111"
    Y la transacción conserva el monto, la moneda y la referencia enviados

  @CP-02 @negativo
  Escenario: Crear transacción sin acceptance_token
    Cuando el comercio intenta crear una transacción de 25000 pesos al número Nequi "3991111111" sin acceptance_token
    Entonces la transacción es rechazada por datos inválidos
    Y el error del campo "acceptance_token" indica "No está presente"

  @CP-03 @negativo
  Escenario: Crear transacción sin datos obligatorios
    Cuando el comercio intenta crear una transacción enviando únicamente el acceptance_token
    Entonces la transacción es rechazada por datos inválidos
    Y se informan como no presentes los campos:
      | amount_in_cents |
      | currency        |
      | reference       |

  @CP-04 @negativo
  Escenario: Crear transacción con firma de integridad inválida
    Cuando el comercio intenta crear una transacción de 25000 pesos al número Nequi "3991111111" con una firma inválida
    Entonces la transacción es rechazada por datos inválidos
    Y el error del campo "signature" indica "La firma es inválida"
