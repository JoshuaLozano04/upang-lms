<?php

namespace App\Http\Controllers;
use Illuminate\Http\Request;
use App\Models\books;
use App\Http\Resources\booksResource;
use Illuminate\Support\Facades\Validator;



class bookscontroller extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
      $books = books::get ();
      if ($books-> count() > 0 )
      {
        return booksResource::collection($books);

      } else {
        return response ()->json (['message' => 'No record Available'] ,200);
      }
    }

    /**
     * Show the form for creating a new resource.
     */
    public function create()
    {
        //
    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request)
    {   

        $validator = Validator::make($request->all(),[
            'Author' => 'required|string|max:255',
            'Publisher' => 'required|string|max:255',
            'Description' => 'required|string|max:255',
            'Bookcover' => 'required|string|max:255',
            'Location' => 'required|string|max:255',
            'Status' => 'required|bolean|max:255',
            'Category' => 'required|string|max:255',
]);  

 if ($validator->fails()) 

 {
  return response () -> json([
    'message' => 'All Fields are Required',
    'error' => $validator -> messages(),
  ], 422);
    
    };
        $books= books::create ([
            'Author' => $request->Author,
            'Publisher' => $request->Publisher,
            'Description' => $request->Description,
            'Bookcover' => $request->Bookcover,
            'Location' => $request->Location,
            'Status' => $request->Status,
            'Category' => $request->Category,
            

        ]);
        return response () ->json ([
        'message' => 'Books Created Successfully',
        'data' => new booksResource ($books)
        ],200);
    }

    /**
     * Display the specified resource.
     */
    public function show(books $books)
    {
        return new booksResource ($books);


    }

    /**
     * Show the form for editing the specified resource.
     */
    public function edit(string $id)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, books $books)
    {
        $validator = Validator::make($request->all(),[
            'Author' => 'required|string|max:255',
            'Publisher' => 'required|string|max:255',
            'Description' => 'required|string|max:255',
            'Bookcover' => 'required|string|max:255',
            'Location' => 'required|string|max:255',
            'Status' => 'required|bolean|max:255',
            'Category' => 'required|string|max:255',
]);  

 if ($validator->fails()) 

 {
  return response () -> json([
    'message' => 'All Fields are Required',
    'error' => $validator -> messages(),
  ], 422);
    
    };
        $books= books::create ([
            'Author' => $request->Author,
            'Publisher' => $request->Publisher,
            'Description' => $request->Description,
            'Bookcover' => $request->Bookcover,
            'Location' => $request->Location,
            'Status' => $request->Status,
            'Category' => $request->Category,
            

        ]);
        return response () ->json ([
        'message' => 'Books Updated Successfully',
        'data' => new booksResource ($books)
        ],200);
    }
        
    

    /**
     * Remove the specified resource from storage.
     */
    public function destroy(books $books)
    { return response () ->json ([
        'message' => 'Books Deleted Successfully',
        'data' => new booksResource ($books)
        ],200);
        
    }

}