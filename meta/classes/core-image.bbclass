# Common code for generating core reference images
#
# Copyright (C) 2007-2011 Linux Foundation

LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

# IMAGE_FEATURES control content of the core reference images
# 
# By default we install task-core-boot and task-base packages - this gives us
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
# - ssh-server-dropbear - SSH server (dropbear)
# - ssh-server-openssh  - SSH server (openssh)
#
PACKAGE_GROUP_apps-console-core = "task-core-apps-console"
PACKAGE_GROUP_x11-base = "task-core-x11-base"
PACKAGE_GROUP_x11-sato = "task-core-x11-sato"
PACKAGE_GROUP_x11-netbook = "task-core-x11-netbook"
PACKAGE_GROUP_apps-x11-core = "task-core-apps-x11-core"
PACKAGE_GROUP_apps-x11-games = "task-core-apps-x11-games"
PACKAGE_GROUP_apps-x11-pimlico = "task-core-apps-x11-pimlico"
PACKAGE_GROUP_tools-debug = "task-core-tools-debug"
PACKAGE_GROUP_tools-profile = "task-core-tools-profile"
PACKAGE_GROUP_tools-testapps = "task-core-tools-testapps"
PACKAGE_GROUP_tools-sdk = "task-core-sdk task-core-standalone-sdk-target"
PACKAGE_GROUP_nfs-server = "task-core-nfs-server"
PACKAGE_GROUP_ssh-server-dropbear = "task-core-ssh-dropbear"
PACKAGE_GROUP_ssh-server-openssh = "task-core-ssh-openssh"
PACKAGE_GROUP_package-management = "${ROOTFS_PKGMANAGE}"
PACKAGE_GROUP_qt4-pkgs = "task-core-qt-demos"

POKY_BASE_INSTALL = '\
    task-core-boot \
    task-base-extended \
    \
    ${@base_contains("IMAGE_FEATURES", "package-management", "", "${ROOTFS_PKGMANAGE_BOOTSTRAP}",d)} \
    \
    ${POKY_EXTRA_INSTALL} \
    '

POKY_EXTRA_INSTALL ?= ""

IMAGE_INSTALL ?= "${POKY_BASE_INSTALL}"

X11_IMAGE_FEATURES  = "x11-base apps-x11-core package-management"
ENHANCED_IMAGE_FEATURES = "${X11_IMAGE_FEATURES} apps-x11-games apps-x11-pimlico package-management"
SATO_IMAGE_FEATURES = "${ENHANCED_IMAGE_FEATURES} x11-sato ssh-server-dropbear"

inherit image

# Create /etc/timestamp during image construction to give a reasonably sane default time setting
ROOTFS_POSTPROCESS_COMMAND += "rootfs_update_timestamp ; "
