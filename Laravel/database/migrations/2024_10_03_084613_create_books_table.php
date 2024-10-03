<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {   
        Schema::create('books', function (Blueprint $table) {
            $table->foreignId('userid')->constrained()->onDelete('cascade');
            $table->string('Author');
            $table->string(column: 'Publisher');
            $table->string('Description');
            $table->string('Bookcover');
            $table->string('Category');
            $table->string('Location');
            $table->boolean('Status');
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('books');
    }
};
