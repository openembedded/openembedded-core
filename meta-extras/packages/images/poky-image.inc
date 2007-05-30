# Common for Poky images
#
# Copyright (C) 2007 OpenedHand LTD

# IMAGE_FEATURES control content of images built with Poky.
# 
# By default we install task-oh-boot and task-oh-boot-extras packages - this
# gives us working (console only) rootfs.
#
# "apps-core", "apps-pda" and other tasks are defined in task-oh recipe and have
# to add needed packages for selected task.
#

DISTRO_TASKS += '\
    ${@base_contains("IMAGE_FEATURES", "dbg-pkgs", "task-oh-boot-dbg task-oh-boot-extras-dbg", "",d)} \
    ${@base_contains("IMAGE_FEATURES", "dev-pkgs", "task-oh-boot-dev task-oh-boot-extras-dev", "",d)} \
	\
    ${@base_contains("IMAGE_FEATURES", "apps-core", "task-oh-base", "",d)} \
    ${@base_contains("IMAGE_FEATURES", ["apps-core", "dbg-pkgs"], "task-oh-base-dbg", "",d)} \
    ${@base_contains("IMAGE_FEATURES", ["apps-core", "dev-pkgs"], "task-oh-base-dev", "",d)} \
	\
    ${@base_contains("IMAGE_FEATURES", "apps-pda", "task-oh-standard", "",d)} \
    ${@base_contains("IMAGE_FEATURES", ["apps-pda", "dbg-pkgs"], "task-oh-standard-dbg", "",d)} \
    ${@base_contains("IMAGE_FEATURES", ["apps-pda", "dev-pkgs"], "task-oh-standard-dev", "",d)} \
    \
    ${@base_contains("IMAGE_FEATURES", "dev-tools", "task-oh-sdk", "",d)} \    
    ${@base_contains("IMAGE_FEATURES", ["dev-tools", "dbg-pkgs"], "task-oh-sdk-dbg", "",d)} \
    ${@base_contains("IMAGE_FEATURES", ["dev-tools", "dev-pkgs"], "task-oh-sdk-dev", "",d)} \
    \
    ${@base_contains("IMAGE_FEATURES", "dbg-tools", "task-oh-devtools", "",d)} \
    ${@base_contains("IMAGE_FEATURES", ["dbg-tools", "dbg-pkgs"], "task-oh-devtools-dbg", "",d)} \
    ${@base_contains("IMAGE_FEATURES", ["dbg-tools", "dev-pkgs"], "task-oh-devtools-dev", "",d)} \
    \
    ${@base_contains("IMAGE_FEATURES", "test-tools", "task-oh-testapps", "",d)} \
    ${@base_contains("IMAGE_FEATURES", ["test-tools", "dbg-pkgs"], "task-oh-testapps-dbg", "",d)} \
    ${@base_contains("IMAGE_FEATURES", ["test-tools", "dev-pkgs"], "task-oh-testapps-dev", "",d)} \
    \
    ${@base_contains("IMAGE_FEATURES", "nfs-server", "task-oh-nfs-server", "",d)} \
    '

IMAGE_INSTALL ?= "${DISTRO_TASKS}"

inherit image
