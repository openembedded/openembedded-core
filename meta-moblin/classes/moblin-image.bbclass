# Common for Moblin images
#
# Copyright (C) 2007 OpenedHand LTD

# IMAGE_FEATURES control content of images built with Poky.
# 
# By default we install task-moblin-boot and task-base packages - this gives us
# working (console only) rootfs.
#
# Available IMAGE_FEATURES:
#
# - apps-console-core
# - x11-base            - X11 server + minimal desktop	
# - x11-sato            - OpenedHand Sato environment
# - x11-netbook         - Metacity based environment for netbooks
# - apps-x11-core       - X Terminal, file manager, file editor
# - apps-x11-games
# - apps-x11-pimlico    - OpenedHand Pimlico apps
# - tools-sdk           - SDK
# - tools-debug         - debugging tools
# - tools-profile       - profiling tools
# - tools-testapps      - tools usable to make some device tests
# - nfs-server          - NFS server (exports / over NFS to everybody)
# - dev-pkgs            - development packages
# - dbg-pkgs            - debug packages
#

MOBLIN_BASE_INSTALL = '\
    task-moblin-boot \
    task-base-extended \
    ${@base_contains("IMAGE_FEATURES", "dbg-pkgs", "task-moblin-boot-dbg task-base-dbg", "",d)} \
    ${@base_contains("IMAGE_FEATURES", "dev-pkgs", "task-moblin-boot-dev task-base-dev", "",d)} \
    \
    ${@base_contains("IMAGE_FEATURES", "apps-console-core", "task-moblin-apps-console", "",d)} \
    ${@base_contains("IMAGE_FEATURES", ["apps-console-core", "dbg-pkgs"], "task-moblin-apps-console-dbg", "",d)} \
    ${@base_contains("IMAGE_FEATURES", ["apps-console-core", "dev-pkgs"], "task-moblin-apps-console-dev", "",d)} \
    \
    ${@base_contains("IMAGE_FEATURES", "x11-base", "task-moblin-x11-base", "",d)} \
    ${@base_contains("IMAGE_FEATURES", ["x11-base", "dbg-pkgs"], "task-moblin-x11-base-dbg", "",d)} \
    ${@base_contains("IMAGE_FEATURES", ["x11-base", "dev-pkgs"], "task-moblin-x11-base-dev", "",d)} \
    \
    ${@base_contains("IMAGE_FEATURES", "x11-sato", "task-moblin-x11-sato", "",d)} \
    ${@base_contains("IMAGE_FEATURES", ["x11-sato", "dbg-pkgs"], "task-moblin-x11-sato-dbg", "",d)} \
    ${@base_contains("IMAGE_FEATURES", ["x11-sato", "dev-pkgs"], "task-moblin-x11-sato-dev", "",d)} \
    \
    ${@base_contains("IMAGE_FEATURES", "x11-netbook", "task-moblin-x11-netbook", "", d)} \
    ${@base_contains("IMAGE_FEATURES", ["x11-netbook", "dbg-pkgs"], "task-moblin-x11-netbook-dbg", "", d)} \
    ${@base_contains("IMAGE_FEATURES", ["x11-netbook", "dev-pkgs"], "task-moblin-x11-netbook-dev", "", d)} \
    ${@base_contains("IMAGE_FEATURES", "apps-x11-core", "task-moblin-apps-x11-core", "",d)} \
    ${@base_contains("IMAGE_FEATURES", ["apps-x11-core", "dbg-pkgs"], "task-moblin-apps-x11-core-dbg", "",d)} \
    ${@base_contains("IMAGE_FEATURES", ["apps-x11-core", "dev-pkgs"], "task-moblin-apps-x11-core-dev", "",d)} \
    \
    ${@base_contains("IMAGE_FEATURES", "apps-x11-games", "task-moblin-apps-x11-games", "",d)} \
    ${@base_contains("IMAGE_FEATURES", ["apps-x11-games", "dbg-pkgs"], "task-moblin-apps-x11-games-dbg", "",d)} \
    ${@base_contains("IMAGE_FEATURES", ["apps-x11-games", "dev-pkgs"], "task-moblin-apps-x11-games-dev", "",d)} \
    \
    ${@base_contains("IMAGE_FEATURES", "apps-x11-pimlico", "task-moblin-apps-x11-pimlico", "",d)} \
    ${@base_contains("IMAGE_FEATURES", ["apps-x11-pimlico", "dbg-pkgs"], "task-moblin-apps-x11-pimlico-dbg", "",d)} \
    ${@base_contains("IMAGE_FEATURES", ["apps-x11-pimlico", "dev-pkgs"], "task-moblin-apps-x11-pimlico-dev", "",d)} \
    \
    ${@base_contains("IMAGE_FEATURES", "tools-debug", "task-moblin-tools-debug", "",d)} \
    ${@base_contains("IMAGE_FEATURES", ["tools-debug", "dbg-pkgs"], "task-moblin-tools-debug-dbg", "",d)} \
    ${@base_contains("IMAGE_FEATURES", ["tools-debug", "dev-pkgs"], "task-moblin-tools-debug-dev", "",d)} \
    \
    ${@base_contains("IMAGE_FEATURES", "tools-profile", "task-moblin-tools-profile", "",d)} \
    ${@base_contains("IMAGE_FEATURES", ["tools-profile", "dbg-pkgs"], "task-moblin-tools-profile-dbg", "",d)} \
    ${@base_contains("IMAGE_FEATURES", ["tools-profile", "dev-pkgs"], "task-moblin-tools-profile-dev", "",d)} \
    \
    ${@base_contains("IMAGE_FEATURES", "tools-testapps", "task-moblin-tools-testapps", "",d)} \
    ${@base_contains("IMAGE_FEATURES", ["tools-testapps", "dbg-pkgs"], "task-moblin-tools-testapps-dbg", "",d)} \
    ${@base_contains("IMAGE_FEATURES", ["tools-testapps", "dev-pkgs"], "task-moblin-tools-testapps-dev", "",d)} \
    \
    ${@base_contains("IMAGE_FEATURES", "tools-sdk", "task-moblin-sdk task-moblin-standalone-sdk-target", "",d)} \    
    ${@base_contains("IMAGE_FEATURES", ["tools-sdk", "dbg-pkgs"], "task-moblin-sdk-dbg", "",d)} \
    ${@base_contains("IMAGE_FEATURES", ["tools-sdk", "dev-pkgs"], "task-moblin-sdk-dev", "",d)} \
    \
    ${@base_contains("IMAGE_FEATURES", "nfs-server", "task-moblin-nfs-server", "",d)} \
    ${@base_contains("IMAGE_FEATURES", ["nfs-server", "dbg-pkgs"], "task-moblin-nfs-server-dbg", "",d)} \

    ${@base_contains("IMAGE_FEATURES", ["nfs-server", "dev-pkgs"], "task-moblin-nfs-server-dev", "",d)} \
    \
    ${@base_contains("IMAGE_FEATURES", "package-management", "${ROOTFS_PKGMANAGE}", "",d)} \
    ${MOBLIN_EXTRA_INSTALL} \
    '

MOBLIN_EXTRA_INSTALL ?= ""

IMAGE_INSTALL ?= "${MOBLIN_BASE_INSTALL}"

X11_IMAGE_FEATURES  = "x11-base apps-x11-core package-management"
ENHANCED_IMAGE_FEATURES = "${X11_IMAGE_FEATURES} apps-x11-games apps-x11-pimlico package-management"
SATO_IMAGE_FEATURES = "${ENHANCED_IMAGE_FEATURES} apps-x11-sato"
NETBOOK_IMAGE_FEATURES = "${ENHANCED_IMAGE_FEATURES} apps-x11-netbook"

inherit image

# Create /etc/timestamp during image construction to give a reasonably sane default time setting
ROOTFS_POSTPROCESS_COMMAND += "rootfs_update_timestamp"
