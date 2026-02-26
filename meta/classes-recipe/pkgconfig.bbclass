#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: MIT
#

DEPENDS:prepend = "pkgconfig-native "

export PKG_CONFIG_DIR ?= "${STAGING_DIR_HOST}${libdir}/pkgconfig"
export PKG_CONFIG_PATH ?= "${PKG_CONFIG_DIR}:${STAGING_DATADIR}/pkgconfig"
export PKG_CONFIG_LIBDIR ?= "${PKG_CONFIG_DIR}"
export PKG_CONFIG_SYSROOT_DIR ?= "${STAGING_DIR_HOST}"
export PKG_CONFIG_DISABLE_UNINSTALLED ?= "yes"
export PKG_CONFIG_SYSTEM_LIBRARY_PATH ?= "${base_libdir}:${libdir}"
export PKG_CONFIG_SYSTEM_INCLUDE_PATH ?= "${includedir}"
