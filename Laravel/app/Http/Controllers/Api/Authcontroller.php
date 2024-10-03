<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;
use App\Models\User;

class Authcontroller extends Controller
{
    public function login(request $request): JsonResponse
    {
    $request ->validate([
        'name' =>'required|string|max:255',
        'email' =>'required|email|max:255',
        'password'=> 'required|string|min:8|max:255'
    ]);


    $user = User::where('email',$request->email)->first();

    if (!$user || !Hash::check($request->password,$user->password)) { 
        return response () -> json ([
       'message' => 'The provided credentials are incorrect'
        ], 401);
    }
   $token = $user->createToken($user->name.'Auth-Token')->plainTextToken;

   return response () -> json ([
    'message' =>'Login Successful',
    'token_type' => 'Bearer',
    'token' => $token,
   ]
   ,200);
}

public function register(Request $request) : JsonResponse
{
    $request ->validate([
        'name' =>'required|string|max:255',
        'email' =>'required|email|unique:users,email|max:255',
        'password'=> 'required|string|min:8|max:255'
    ]);

    $user = User::create ([
    'name' => $request->name,
    'email' => $request->email,
    'password' => Hash::make ($request->password),  
    
    ]);
    if ($user)
    {
        $token = $user->createToken($user->name.'Auth-Token')->plainTextToken;

        return response () -> json ([
         'message' =>'Register Successful',
         'token_type' => 'Bearer',
         'token' => $token,
        ]
        ,200);

    }
    else 
    {
        return response ()->json([
        'message' =>'Something went wrong while registration.',
        ],500);
    }
    

}

public function logout (request $request):JsonResponse
{
    $user = User::where('email', $request->user()->email)->first();
    if ($user)
    {
        $user->tokens()->delete();
        return response()->json([ 
            'message' => 'Logged out Successfully',
        ], 401);
    } else {
        return response()->json([
            'message'=> 'User Not Found',
        ],404);
    }
}


public function profile (Request $request)
{
    if($request->user())
    {
        return response()->json([ 
            'message' => 'Profile Fetched',
            'data' => $request->user(), 
        ], 200);
        
    } 
    else 
    {
        
        return response()->json ([
            'message' =>'Not Authenticated' 
        ],401);
    }
    
}
};

