require shadow.inc

PR = "r1"

SRC_URI += "file://shadow.automake-1.11.patch \
	    file://shadow-4.1.3-dots-in-usernames.patch \
	    file://shadow-4.1.4.2-env-reset-keep-locale.patch \
	    file://shadow-4.1.4.2-groupmod-pam-check.patch \
	    file://shadow-4.1.4.2-su_no_sanitize_env.patch"

SRC_URI[md5sum] = "b8608d8294ac88974f27b20f991c0e79"
SRC_URI[sha256sum] = "633f5bb4ea0c88c55f3642c97f9d25cbef74f82e0b4cf8d54e7ad6f9f9caa778"

EXTRA_OECONF_libc-uclibc += " --with-nscd=no "
