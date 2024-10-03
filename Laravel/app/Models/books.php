<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class books extends Model
{
    use HasFactory;

    // This is not necessary if your table is 'books', as Laravel will infer it
    protected $table = 'books'; 

    // These are the fields that are mass assignable
    protected $fillable = [
        'Author',
        'Publisher',
        'Description',
        'Bookcover',
        'Category',
        'Location',
        'Status',
    ];

    // Cast the 'Status' column to boolean
    protected $casts = [
        'Status' => 'boolean',
    ];
}
