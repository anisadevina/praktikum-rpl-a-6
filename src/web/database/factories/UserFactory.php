<?php

namespace Database\Factories;

use App\Models\User;
use Illuminate\Database\Eloquent\Factories\Factory;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Str;
use Illuminate\Support\Facades\DB;

/**
 * @extends Factory<User>
 */
class UserFactory extends Factory
{
    /**
     * The current password being used by the factory.
     */
    protected static ?string $password;

    /**
     * Define the model's default state.
     *
     * @return array<string, mixed>
     */
    public function definition(): array
    {
        $nim = fake()->unique()->numerify('L#######');

        $prodiPilihan = ['Informatika', 'Sains Data'];

        // Suntikkan ke tabel master agar relasi Foreign Key aman
        DB::table('master_mahasiswa_fatisda')->insertOrIgnore([
            'nim' => $nim,
            'nama' => fake()->name(),
            'email_institusi' => fake()->unique()->safeEmail(),
            'prodi' => fake()->randomElement($prodiPilihan),
            'tahun_angkatan' => fake()->numberBetween(2020, 2025),

        ]);

        return [
            'nim' => $nim,
            'username' => fake()->unique()->userName(),
            'email_user' => fake()->unique()->safeEmail(),
            'password' => bcrypt('password123'),
            'role' => 'user',
        ];
    }
}
