DESCRIPTION = "X font aliases."

require xorg-font-common.inc

LICENSE = "Font-Alias"
LIC_FILES_CHKSUM = "file://COPYING;md5=bf0158b89be493d523d69d9f29265038 \
                    file://cyrillic/fonts.alias;md5=f40795b0640d6785826aecd3b16f6124 \
                    file://75dpi/fonts.alias;md5=6bc48023f2ae7f3bfc105db7b0ee6b49 \
                    file://misc/fonts.alias;md5=a8ec05d528431d4c9703b55a7efd67a8 \
                    file://100dpi/fonts.alias;md5=85bebd6ca213aa656c301a72eb4397cb"

SRC_URI += "file://fix_COPYING_1f8141a5e3b585dfccc748400b05c9c24b41e767.patch"

DEPENDS = "virtual/xserver font-util"
RDEPENDS = "encodings font-util"
RDEPENDS_virtclass-native = "font-util"

PE = "1"
PR = "${INC_PR}.0"
