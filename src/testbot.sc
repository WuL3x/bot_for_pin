require: slotfilling/slotFilling.sc
  module = sys.zb-common
  
patterns:
    $hello = (привет | добр* (утро/день/вечер) | здравствуй*)
    $yes = (да)
    $no = (нет)
 
theme: /
    state: Start
        q!: $regex</start>
        a: поехали
        
    state: hello
        q!: {{$hello}}
        a: Привет привет
        a: Ты ходил сегодня в зал?
    
    state: yes
        q!:{{$yes}}
        go!: /yeshodil
        
    state: no
        q!:{{$no}}
        go!: /nonehodil
        
    state: yeshodil
        a: Молодец
        
    state: nonehodil
        a: Плохо. Завтра сходи
  
  