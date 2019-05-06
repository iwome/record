import 'package:flutter/material.dart';

class NewRoute extends StatelessWidget {
  String _showContent;

  NewRoute(this._showContent);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("new route"),
      ),
      body: Center(
        child: Text(_showContent),
      ),
    );
  }
}
