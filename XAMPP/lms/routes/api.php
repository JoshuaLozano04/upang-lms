<?php


use App\Http\Controllers\Libraryuserscontroller;
use Illuminate\Support\Facades\Route;
use Illuminate\Http\Request;
use App\Http\Controllers\libraryusers;


Route::get('/libraryusers', [Libraryuserscontroller::class,'index']);

Route::post('/libraryusers', [Libraryuserscontroller::class,'store']);
Route::get('/', function () {
    return "Hello World";
});

Route::get('/user', function (Request $request) {
    return $request->user();
})->middleware('auth:sanctum');
