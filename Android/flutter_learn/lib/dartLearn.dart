//dart语法学习
import 'dart:math';

class Bicycle {
  //无 public protect private修饰符
  int cadence;
  int _speed;
  int gear;

  //当{}为空时，可省略，后面加上分号。
  //通过this关键字可直接对成员变量赋值
  Bicycle(this.cadence, this._speed, this.gear);

  //支持字符串模板${expression}
  //函数体只有一行时，可简写如下
  @override
  String toString() => 'Bicycle :$_speed mph';

  //get方法的声明
  int get speed => _speed;
}

class Rectangle {
  int width;
  int height;
  Point origin;

  //{}表示可选的命名参数，在java中必须要确定参数数量，所以需要用多个构造才行
  //成员对象默认值必须是编译器就确认的常量
  Rectangle({this.origin = const Point(0, 0), this.width = 0, this.height = 0});

  //可选命名参数 {}表示 传参时必须使用参数名：value的形式
  void nameMethod({String a, int b}) {}

  //可选位置参数 []表示
  //可选参数可以有默认值
  //命名和位置参数区别，前者可放在前面，后者只能放在最后，所以前者要命名后者无需
  void positionMethod(String a, int b, [int c = 9, int d]) {}

  @override
  String toString() =>
      'Origin: (${origin.x}, ${origin.y}), width: $width, height: $height';
}

abstract class Shape {
  num get area;

  //创建工厂模式的构造方法
  factory Shape(String type) {
    if (type == 'circle') return Circle(2);
    if (type == 'square') return Square(2);
    if (type == 'circleMock') return CircleMock();
    throw 'Can\'t create $type.';
  }
}

class Circle implements Shape {
  final num radius;

  Circle(this.radius);

  num get area => pi * pow(radius, 2);

  void drawCircle() {}
}

class Square implements Shape {
  final num side;

  Square(this.side);

  num get area => pow(side, 2);
}

//没有interface关键字，每一个类都隐式定义了一个接口
class CircleMock implements Circle {
  num area;

  num radius;

  int color;

  void methodA() {}

  void methodB() {}

  @override
  void drawCircle() {
    // TODO: implement _drawCircle
  }
}

//主函数，函数、变量等可声明在类外
void main() {
  //关键字 new 可省略
  var bike = Bicycle(2, 0, 1);
  var rectangle = Rectangle(origin: const Point(10, 10));
  print(rectangle);
  rectangle.nameMethod(a: "name");
  rectangle.positionMethod("a", 9, 10);
  print(bike);
  CircleMock cm = Shape('circleMock');
  //..表示 级联函数
  cm
    ..methodA()
    ..methodB()
    ..area = 9
    ..color = 6;
  print(Shape('circle').area);
  print(Shape('square').area);
}
