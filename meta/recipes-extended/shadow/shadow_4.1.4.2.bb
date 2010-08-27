require shadow.inc

PR = "r0"

SRC_URI += "file://shadow.automake-1.11.patch \
	    file://shadow-4.1.3-dots-in-usernames.patch \
	    file://shadow-4.1.4.2-env-reset-keep-locale.patch \
	    file://shadow-4.1.4.2-groupmod-pam-check.patch \
	    file://shadow-4.1.4.2-su_no_sanitize_env.patch"

EXTRA_OECONF_libc-uclibc += " --with-nscd=no "
