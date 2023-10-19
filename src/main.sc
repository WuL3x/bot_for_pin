require: slotfilling/slotFilling.sc
  module = sys.zb-common

patterns:
    $app = ((прил* | 1 | логин* | *ход* | *Pin*)*~приложение)
    $card = (((([для] карт* )| 2 | дебет* | *Pin*)*~карта) *)
    $password = (* (парол* | пин*))


theme: /
    
    state: Start
        q!: *start
        a: Начнем

    state: change || modal = true
        q!: {{$password}}
        a:Сейчас расскажу порядок действия. Выберите, что именно планируете сделать: 
                1) Поменять пароль для входа в приложение. 
                2) Поменять PIN-код для карты.
        script:
            $reactions.timeout({interval: "5 seconds", targetState: "/NoAnswer"})

        state: app
            q: {{$app}}
            go!: /changeapp
            
        state: card
            q: {{$card}}
            go!: /changecard
            
        state: LocalCatchAll
            event: noMatch
            a: Это не похоже на ответ. Попробуйте еще раз.
        
        

    state: changeapp 
        intent: /приложение
        a: Смена пароля от приложения возможна несколькими способами:
                1. на экране "Профиль" выберите "Изменить код для входа в приложение".
                2. введите SMS-код.
                3. придумайте новый код для входа в приложение и повторите его.
        script:
            $reactions.timeout({interval: "2 seconds", targetState: './secApp'})
                
        state: secApp 
            a: Либо нажмите на кнопку "Выйти" на старнице ввода пароля для входа в приложение.
                После чего нужно будет заново пройти регистрацию:
                1. ввести полный номер карты(если оформляли ранее, иначе номер телефона и дату рождения),
                2. указать код из смс-код,
                3. придумать нвоый пароль для входа.
            script:
                $reactions.timeout({interval: "2 seconds", targetState: '/Bye'})

                    

    state: changecard 
        intent: /карта
        a: Это можно сделать в приложении:
            1. На экране "Мои деньги" в разделе "Карты" нажмите на нужную.
            2. Выберите вкладку "Настройки". 
            3. Нажмите "Сменить пин-код".
            4. И введите комбинацию, удобную вам.
            5. Повторите ее.
        script:
                $reactions.timeout({interval: "2 seconds", targetState: './secCard'})  
                    
        state: secCard 
            a: И все готово!
                Пин-код установлен, можно пользоваться.
            script:
                $reactions.timeout({interval: "2 seconds", targetState: '/Bye'})
    

    state: Bye
        a: Приятно было пообщаться. Всегда готов помочь вам снова.
    
    state: NoAnswer
        a: Не получил от вас ответа. Всего хорошего!

    state: NoMatch
        event!: noMatch
        a: Я не понял. Вы сказали: {{$request.query}}

    state: Match
        event!: match
        a: {{$context.intent.answer}}

    

    