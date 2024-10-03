<?php

namespace App\Http\Resources;

use Illuminate\Http\Request;
use Illuminate\Http\Resources\Json\JsonResource;

class booksResource extends JsonResource
{
    /**
     * Transform the resource into an array.
     *
     * @return array<string, mixed>
     */
    public function toArray(Request $request): array
    {
       return [
        'id'         => $this->id,
        'Author'     => $this->Author,
        'Publisher'  => $this->Publisher,
        'Bookcover'  => $this->Bookcover,   
        'Category'   => $this->Category,
        'Location'   => $this->Location,
        'Status'     => $this->true, // or false,
       ];
    }
}
