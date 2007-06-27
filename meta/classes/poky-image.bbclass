# Common for Poky images
#
# Copyright (C) 2007 OpenedHand LTD

# IMAGE_FEATURES control content of images built with Poky.
# 
# By default we install task-poky-boot and task-poky-boot-extras packages - this
# gives us working (console only) rootfs.
#
# "apps-core", "apps-pda" and other tasks are defined in task-poky recipe and have
# to add needed packages for selected task.
#

DISTRO_TASKS += '\
    ${@base_contains("IMAGE_FEATURES", "dbg-pkgs", "task-poky-boot-dbg task-poky-boot-extras-dbg", "",d)} \
    ${@base_contains("IMAGE_FEATURES", "dev-pkgs", "task-poky-boot-dev task-poky-boot-extras-dev", "",d)} \
	\
    ${@base_contains("IMAGE_FEATURES", "apps-core", "task-poky-base", "",d)} \
    ${@base_contains("IMAGE_FEATURES", ["apps-core", "dbg-pkgs"], "task-poky-base-dbg", "",d)} \
    ${@base_contains("IMAGE_FEATURES", ["apps-core", "dev-pkgs"], "task-poky-base-dev", "",d)} \
	\
    ${@base_contains("IMAGE_FEATURES", "apps-pda", "task-poky-standard", "",d)} \
    ${@base_contains("IMAGE_FEATURES", ["apps-pda", "dbg-pkgs"], "task-poky-standard-dbg", "",d)} \
    ${@base_contains("IMAGE_FEATURES", ["apps-pda", "dev-pkgs"], "task-poky-standard-dev", "",d)} \
    \
    ${@base_contains("IMAGE_FEATURES", "dev-tools", "task-poky-sdk", "",d)} \    
    ${@base_contains("IMAGE_FEATURES", ["dev-tools", "dbg-pkgs"], "task-poky-sdk-dbg", "",d)} \
    ${@base_contains("IMAGE_FEATURES", ["dev-tools", "dev-pkgs"], "task-poky-sdk-dev", "",d)} \
    \
    ${@base_contains("IMAGE_FEATURES", "dbg-tools", "task-poky-devtools", "",d)} \
    ${@base_contains("IMAGE_FEATURES", ["dbg-tools", "dbg-pkgs"], "task-poky-devtools-dbg", "",d)} \
    ${@base_contains("IMAGE_FEATURES", ["dbg-tools", "dev-pkgs"], "task-poky-devtools-dev", "",d)} \
    \
    ${@base_contains("IMAGE_FEATURES", "test-tools", "task-poky-testapps", "",d)} \
    ${@base_contains("IMAGE_FEATURES", ["test-tools", "dbg-pkgs"], "task-poky-testapps-dbg", "",d)} \
    ${@base_contains("IMAGE_FEATURES", ["test-tools", "dev-pkgs"], "task-poky-testapps-dev", "",d)} \
    \
    ${@base_contains("IMAGE_FEATURES", "nfs-server", "task-poky-nfs-server", "",d)} \
    '

IMAGE_INSTALL ?= "${DISTRO_TASKS}"

inherit image
