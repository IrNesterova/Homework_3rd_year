import React, {Component} from 'react';
import logo from './logo.svg';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import { BrowserRouter as Router, Switch, Route, Link } from "react-router-dom";
import Login from "./components/Login/Login";
import SignUp from "./components/Registration/Registration";
import Chat from "./components/Chat/Chat"
function App() {
    return(
<Router>
<Switch>
<Route exact path='/' component={Login} />
<Route path="/login" component={Login} />
<Route path="/sign-up" component={SignUp} />
<Route path="/chat" component={Chat}/>
</Switch>
</Router>
    )
}

export default App;