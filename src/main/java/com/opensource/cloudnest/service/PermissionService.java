package com.opensource.cloudnest.service;

import com.opensource.cloudnest.dto.PermissionDTO;
import com.opensource.cloudnest.dto.PermissionRequestDto;
import com.opensource.cloudnest.dto.ResDTO;
import com.opensource.cloudnest.dto.response.*;
import com.opensource.cloudnest.entity.Permission;
import com.opensource.cloudnest.entity.Role;
import com.opensource.cloudnest.repository.PermissionRepository;
import com.opensource.cloudnest.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Autowired
    private RoleService roleService;

    public ResDTO<Object> addPermissionName(String permissionName , String category , String description) {
        Permission permission = new Permission();
        permission.setName(permissionName);
        permission.setCategory(category);
        permission.setDescription(description);
        permission.setCreatedAt(LocalDateTime.now());
        permissionRepository.save(permission);
        return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS, "Permissions added Successfully");
    }


    public ResDTO<Object> getPermissions() {
        List<Permission> permissions = permissionRepository.findAll(); // Get all permissions>
        return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS, permissions);
    }

    public ResDTO<Object> addPermission(PermissionDTO permissionDTO) {
        Map<Long, List<Long>> rolePermissionsMap = permissionDTO.getRolePermissionsMap();
        // Assuming `PermissionDTO` contains a map where the key is the roleId and the value is a list of permissionIds

        for (Map.Entry<Long, List<Long>> entry : rolePermissionsMap.entrySet()) {
            Long roleId = entry.getKey();
            List<Long> permissionIds = entry.getValue();

            // Fetch the Role entity
            Optional<Role> roleOpt = roleRepository.findById(roleId);
            if (roleOpt.isPresent()) {
                Role role = roleOpt.get();

                // Clear existing permissions
                role.getPermissions().clear();

                // Fetch and add new permissions for this role
                for (Long permissionId : permissionIds) {
                    Optional<Permission> permissionOpt = permissionRepository.findById(permissionId);

                    if (permissionOpt.isPresent()) {
                        Permission permission = permissionOpt.get();
                        role.getPermissions().add(permission); // Assign the permission

                    } else {
                        throw new RuntimeException("Permission with ID " + permissionId + " not found");
                    }
                }

                // Save the updated role with the new permissions
                roleRepository.save(role);
            } else {
                // Handle the case where the role doesn't exist
                throw new RuntimeException("Role with ID " + roleId + " not found");
            }
        }

        return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS, "Permissions added/updated successfully");
    }

    /*public ResDTO<Object> revokePermission(String roleName) {
        Optional<Role> optionalRole = roleRepository.findByName(roleName);
        if(optionalRole.isPresent()) {
            Optional<Permission> optionalPermission = permissionRepository.findByRole(optionalRole.get());
            optionalPermission.ifPresent(permission -> permissionRepository.delete(permission));
        }
        else {
            return new ResDTO<>(Boolean.TRUE , ResDTOMessage.RECORD_NOT_FOUND , "Permissions does not exists for the given role");
        }
        return new ResDTO<>(Boolean.TRUE , ResDTOMessage.SUCCESS , "Permissions revoked Successfully for role" + roleName);
    }*/

    public List<PermissionResponse> getAccessiblePermissionsByRole(String roleName) {
        Optional<Role> optionalRole = roleRepository.findByName(roleName);
        if (optionalRole.isPresent()) {
            Set<Permission> permissions = optionalRole.get().getPermissions(); // Get the permissions associated with the role>
            return createPermissionResponse(permissions);
        }
        return null;
    }

    public List<MenuObjectResponse> getMenuResponse(String roleName) {
        Optional<Role> optionalRole = roleRepository.findByName(roleName);
        List<MenuObjectResponse> resultList = new ArrayList<>();

        Map<String , List<String>> menuListResponseResult = new HashMap<>();
        Set<String> category = new HashSet<>();
         if (optionalRole.isPresent()) {
            Set<Permission> permissions = optionalRole.get().getPermissions();
             Map<String , List<Permission>> menuListResponse =permissions.stream().collect(groupingBy(Permission::getCategory));

                    //posts.stream()
            for(String key :menuListResponse.keySet()){

                List<String> permissionResult = new ArrayList<>();
                for(Permission p :  menuListResponse.get(key)){
                    permissionResult.add(p.getName());

                }
                menuListResponseResult.put(key,permissionResult);
            }
            for(String key: menuListResponseResult.keySet()){
                MenuObjectResponse menuObjectResponse = new MenuObjectResponse();
                menuObjectResponse.setMenu(key);
                List<String> permissionList = menuListResponseResult.get(key);
                for(String name : permissionList){
                    if(name.contains("CREATE")){
                        menuObjectResponse.setCrud(true);
                        break;
                    }
                }
                resultList.add(menuObjectResponse);
                permissionList.clear();
            }


            // Get the permissions associated with the role>
//            for(Permission permission :permissions) {
//
//                if(permission.getName().contains("VIEW")) {
//                    menuListResponse.add(permission.getCategory(),)
//
//                    category.add(permission.getCategory());
//                }
//            }
        }
        return resultList;
    }

    private List<PermissionResponse> createPermissionResponse(Set<Permission> permissions) {
        List<PermissionResponse> permissionResponses = new ArrayList<>();
        for (Permission permission : permissions) {
            PermissionResponse permissionResponse = new PermissionResponse();
            permissionResponse.setName(permission.getName());
            permissionResponse.setId(permission.getId());
            permissionResponses.add(permissionResponse);
        }
        return permissionResponses;
    }

    public ResDTO<Object> getAllPermissionsWithRoles() {
        // Fetch all roles and permissions from the database

        HashMap<String, Object> resultMap = new HashMap<>();
        List<Role> allRoles = roleRepository.findAll();
        List<Permission> allPermissions = permissionRepository.findAll();

        // Define categories
        List<String> categories = Arrays.asList("User", "Role", "Permission", "Knowledgevault", "Flowchain", "Integrations");

        // Initialize the response structure
        List<Map<String, Object>> groupedPermissions = new ArrayList<>();

        // Iterate through categories and group permissions
        for (String category : categories) {
            // Filter permissions belonging to the current category
            List<Map<String, Object>> permissionsForCategory = allPermissions.stream()
                    .filter(permission -> permission.getName().toUpperCase().contains(category.toUpperCase())) // Match category in the permission name
                    .map(permission -> {
                        // Find roles associated with this permission
                        List<Map<String, Object>> associatedRoles = allRoles.stream()
                                .filter(role -> role.getPermissions().contains(permission)) // Check if the role has this permission
                                .map(role -> {
                                    Map<String, Object> roleData = new HashMap<>();
                                    roleData.put("id", role.getId());
                                    roleData.put("name", role.getName());
                                    return roleData;
                                })
                                .collect(Collectors.toList());

                        // Build the permission structure
                        Map<String, Object> permissionData = new HashMap<>();
                        permissionData.put("PermissionName", permission.getName());
                        permissionData.put("roles", associatedRoles);
                        permissionData.put("PermissionId", permission.getId());
                        return permissionData;
                    })
                    .collect(Collectors.toList());

            // Add the category and its permissions to the response
            Map<String, Object> categoryData = new HashMap<>();
            categoryData.put(category, permissionsForCategory);
            groupedPermissions.add(categoryData);
        }

        // Create the final response payload
        return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS, groupedPermissions);
    }


    public ResDTO<Object> getAllPermissionsWithRolesAlpha() {
        // Fetch all roles and permissions from the database
        HashMap<String, Object> resultMap = new HashMap<>();

        List<String> categories = Arrays.asList("User", "Role", "Permission", "Knowledgevault", "Flowchain", "Integrations");

        List<Role> roles = roleRepository.findAll();

        List<HashMap<String, Object>> rowItem = new ArrayList<>();



        for (String category : categories) {

            List<HashMap<String, Object>> permissionItems = new ArrayList<>();

            List<Permission> categoryPermissions = permissionRepository.findByCategory(category);


            for (Permission permission : categoryPermissions) {

                HashMap<String, Object> PermaisshashMap = new HashMap<>();

                for (Role role : roles) {
                    PermaisshashMap.put(role.getName().toLowerCase(), role.getPermissions().contains(permission));
                }

                PermaisshashMap.put("name",permission.getName().split("_")[0].toLowerCase());
                HashMap<String, Object> somehashMap = new HashMap<>();


                somehashMap.put(permission.getName(), PermaisshashMap);

                permissionItems.add(PermaisshashMap);


            }
            HashMap<String, Object> hsh = new HashMap<>();
            hsh.put("name", category);
            hsh.put("subMenu", permissionItems);
            rowItem.add(hsh);

        }


        List<ColumnResponseDto> columnResponseDtos = new ArrayList<>();
        ColumnResponseDto columnResponseDto = new ColumnResponseDto();
        columnResponseDto.setAccessor("name");
        columnResponseDto.setLabel("Name");
        columnResponseDtos.add(columnResponseDto);
        for (Role role : roles) {
            columnResponseDto = new ColumnResponseDto();
            columnResponseDto.setAccessor(role.getName().toLowerCase());
            columnResponseDto.setLabel(role.getName().replace("_"," "));
            columnResponseDtos.add(columnResponseDto);
        }

        //return
        resultMap.put("columns", columnResponseDtos);
        resultMap.put("rows",rowItem);

        return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS, resultMap);

    }

    public ResDTO<Object> addPermissionAlpha(List<PermissionRequestDto> permissionRequestDtos) {


        for (PermissionRequestDto permissionRequestDto : permissionRequestDtos) {

            String permissionName = permissionRequestDto.getName().toUpperCase();
           // Optional<Permission> permission = permissionRepository.findByName(permissionRequestDto.getPermission());
            List<HashMap<String, Object>> lists = permissionRequestDto.getSubMenu();

            for (HashMap<String, Object> hashmap : lists) {

                String name = hashmap.get("name").toString().toUpperCase() + "_" + permissionName;
                for (String key : hashmap.keySet()) {

                    Optional<Permission> permission = permissionRepository.findByName(name);

                    List<Role> role = roleRepository.findByNameContainingIgnoreCase(key);

                    if (hashmap.get(key).equals(true)) {
                        roleService.addPermissionToRole(role.get(0), permission.get());
                    }
                    else if(hashmap.get(key).equals(false)) {
                        roleService.deletePermissionToRole(role.get(0), permission.get());

                    }
                }
            }
        }

        return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS, "Permissions added/updated successfully");
    }
}
