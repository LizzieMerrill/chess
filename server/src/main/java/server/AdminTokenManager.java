package server;

import java.util.HashSet;
import java.util.Set;

// Assuming you have a class that manages administrator tokens
// Replace this with your actual implementation for checking admin tokens
class AdminTokenManager {
    private static final Set<String> adminTokens = new HashSet<>();

    static boolean isAdminAuthToken(String authToken) {
        // Add some predefined admin tokens if the set is empty
        if (adminTokens.isEmpty()) {
            adminTokens.add("adminToken1");
            adminTokens.add("adminToken2");
            // Add more as needed
        }

        return adminTokens.contains(authToken);
    }
}
//this was all an attempt to fix spark.delete /db for the default clear test