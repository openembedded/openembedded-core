SUMMARY = "libsecret is a library for storing and retrieving passwords and other secrets"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=23c2a5e0106b99d75238986559bb5fc6"

inherit gnomebase gtk-doc

DEPENDS = "glib-2.0 libgcrypt gettext-native intltool-native"

EXTRA_OECONF += "--disable-manpages"

SRC_URI[archive.md5sum] = "5190da1ee686437046bc10068f120d1d"
SRC_URI[archive.sha256sum] = "0f29b51698198e6999c91f4adce3119c8c457f546b133a85baea5ea9010a19ed"

# http://errors.yoctoproject.org/Errors/Details/20228/
ARM_INSTRUCTION_SET = "arm"
