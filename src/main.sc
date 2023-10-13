require: slotfilling/slotFilling.sc
  module = sys.zb-common
require: functions.js

patterns:
    $app = (прило*)
    $card = (карт)

theme: /

    state: Start
        q!: $regex</start>
        a: Начнем
            

    state: hello
        intent!: /привет
        a: Привет


    state: change || modal = true
        intentGroup!: /пароль
        
        script:
            $session.text = $request.query;
            # if ($session.text == "прило" || 1){
            #     $reactions.transition("/.changeapp")
            #     }
            $reactions.answer($session.text)

        a: {{$request.query}}
        if: $request.query = $app
            go!: /change/changeapp
        if: $request.query = $card
            go!: /change/changeapp

 

        state: changeapp
            intent: /пароль/приложение
            a: Смена пароля от приложения возможна несколькими способами:
                    1. на экране "Профиль" выберите "Изменить код для входа в приложение".
                    2. введите SMS-код.
                    3. придумайте новый код для входа в приложение и повторите его.
            script:
                $reactions.timeout({interval: "2 seconds", targetState: "./secApp"})
                
            state: secApp
                a: Либо нажмите на кнопку "Выйти" на старнице ввода пароля для входа в приложение.
                    После чего нужно будет заново пройти регистрацию:
                    1. ввести полный номер карты(если оформляли ранее, иначе номер телефона и дату рождения),
                    2. указать код из смс-код,
                    3. придумать нвоый пароль для входа.
                script:
                    $reactions.timeout({interval: "2 seconds", targetState: "./thrApp"})
                
                state: thrApp
                    a:Приятно было пообщаться. Всегда готов помочь вам снова!
                    

        state: changecard
            intent: /пароль/карта
            a: Это можно сделать в приложении:
                1. На экране "Мои деньги" в разделе "Карты" нажмите на нужную.
                2. Выберите вкладку "Настройки". 
                3. Нажмите "Сменить пин-код".
                4. И введите комбинацию, удобную вам.
                5. Повторите ее.
            script:
                    $reactions.timeout({interval: "2 seconds", targetState: "./secCard"})  
                    
            state: secCard
                a: И все готово!
                    Пин-код установлен, можно пользоваться.
                a: Приятно было пообщаться. Всегда готов помочь вам снова.

    state: Bye
        intent!: /пока
        a: Пока пока

    state: NoMatch
        event!: noMatch
        a: Я не понял. Вы сказали: {{$request.query}}

    state: Match
        event!: match
        a: {{$context.intent.answer}}

    

    