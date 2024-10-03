<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\libraryusers;
class Libraryuserscontroller extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        return libraryusers::all();

    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request)
    {
        $request ->validate([
            'fullname' => 'required',
             'email' => 'required',
             'password'=> 'required',
        ]);
        return libraryusers::create($request->all());
    }

    /**
     * Display the specified resource.
     */
    public function show(string $id)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, string $id)
    {
        //
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy(string $id)
    {
        //
    }
}
