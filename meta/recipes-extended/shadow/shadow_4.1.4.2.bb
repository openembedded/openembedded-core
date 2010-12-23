require shadow.inc

PR = "r1"

SRC_URI += "file://shadow.automake-1.11.patch \
	    file://shadow-4.1.3-dots-in-usernames.patch \
	    file://shadow-4.1.4.2-env-reset-keep-locale.patch \
	    file://shadow-4.1.4.2-groupmod-pam-check.patch \
	    file://shadow-4.1.4.2-su_no_sanitize_env.patch"

SRC_URI[md5sum] = "d593a9cab93c48ee0a6ba056db8c1997"
SRC_URI[sha256sum] = "97987f6a7967a85e6aa0dba2a1d52db8bd69af5a717391de5693db768fb78990"

EXTRA_OECONF_libc-uclibc += " --with-nscd=no "
