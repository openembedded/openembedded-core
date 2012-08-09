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
# - x11-mini            - minimal environment for X11 server 
# - x11-base            - X11 server + minimal desktop	
# - x11-sato            - OpenedHand Sato environment
# - apps-x11-core       - X Terminal, file manager, file editor
# - apps-x11-games
# - tools-sdk           - SDK
# - tools-debug         - debugging tools
# - tools-profile       - profiling tools
# - tools-testapps      - tools usable to make some device tests
# - nfs-server          - NFS server (exports / over NFS to everybody)
# - ssh-server-dropbear - SSH server (dropbear)
# - ssh-server-openssh  - SSH server (openssh)
# - debug-tweaks        - makes an image suitable for development
#
PACKAGE_GROUP_apps-console-core = "task-core-apps-console"
PACKAGE_GROUP_x11-mini = "task-core-x11-mini"
PACKAGE_GROUP_x11-base = "task-core-x11-base"
PACKAGE_GROUP_x11-sato = "task-core-x11-sato"
PACKAGE_GROUP_apps-x11-core = "task-core-apps-x11-core"
PACKAGE_GROUP_apps-x11-games = "task-core-apps-x11-games"
PACKAGE_GROUP_tools-debug = "task-core-tools-debug"
PACKAGE_GROUP_tools-profile = "task-core-tools-profile"
PACKAGE_GROUP_tools-testapps = "task-core-tools-testapps"
PACKAGE_GROUP_tools-sdk = "task-core-sdk task-core-standalone-sdk-target"
PACKAGE_GROUP_nfs-server = "task-core-nfs-server"
PACKAGE_GROUP_ssh-server-dropbear = "task-core-ssh-dropbear"
PACKAGE_GROUP_ssh-server-openssh = "task-core-ssh-openssh"
PACKAGE_GROUP_package-management = "${ROOTFS_PKGMANAGE}"
PACKAGE_GROUP_qt4-pkgs = "task-core-qt-demos"


# IMAGE_FEATURES_REPLACES_foo = 'bar1 bar2'
# Including image feature foo would replace the image features bar1 and bar2
IMAGE_FEATURES_REPLACES_ssh-server-openssh = "ssh-server-dropbear"

# IMAGE_FEATURES_CONFLICTS_foo = 'bar1 bar2'
# An error exception would be raised if both image features foo and bar1(or bar2) are included

CORE_IMAGE_BASE_INSTALL = '\
    task-core-boot \
    task-base-extended \
    \
    ${CORE_IMAGE_EXTRA_INSTALL} \
    '

CORE_IMAGE_EXTRA_INSTALL ?= ""

IMAGE_INSTALL ?= "${CORE_IMAGE_BASE_INSTALL}"

X11_IMAGE_FEATURES  = "x11-base apps-x11-core package-management"
ENHANCED_IMAGE_FEATURES = "${X11_IMAGE_FEATURES} apps-x11-games package-management"
SSHSERVER_IMAGE_FEATURES ??= "ssh-server-dropbear"
SATO_IMAGE_FEATURES = "${ENHANCED_IMAGE_FEATURES} x11-sato ${SSHSERVER_IMAGE_FEATURES}"

inherit image

# Create /etc/timestamp during image construction to give a reasonably sane default time setting
ROOTFS_POSTPROCESS_COMMAND += "rootfs_update_timestamp ; "

# Zap the root password if debug-tweaks feature is not enabled
ROOTFS_POSTPROCESS_COMMAND += '${@base_contains("IMAGE_FEATURES", "debug-tweaks", "", "zap_root_password ; ",d)}'
# Allow openssh accept empty password login if both debug-tweaks and ssh-server-openssh are enabled
ROOTFS_POSTPROCESS_COMMAND += '${@base_contains("IMAGE_FEATURES", "debug-tweaks ssh-server-openssh", "openssh_allow_empty_password; ", "",d)}'

