<?php


// routes/web.php

use App\Http\Controllers\Api\Authcontroller;
use App\Http\Controllers\bookscontroller;
use Illuminate\Support\Facades\Route;
use Illuminate\Http\Request;


Route::post('/login', [AuthController::class, 'login']);
Route::post('/register', [AuthController::class, 'register']);
 Route::middleware (['auth:sanctum',]) ->group(function () {
 Route::post ('/logout', [AuthController::class,'logout']);
 Route::get ('/profile', [AuthController::class,'profile']);
 Route::apiResource('books', bookscontroller::class);


});






Route::get('/', function () {
    return "Connected Successfully";
});

Route::get('/user', function (Request $request) {
    return $request->user();
})->middleware(middleware: 'auth:sanctum');

