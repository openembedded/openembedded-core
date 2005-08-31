include gettext_${PV}.bb
S = "${WORKDIR}/gettext-${PV}"
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/gettext-${PV}"
inherit native
PROVIDES = ""

M4 = "\
lib-ld.m4 \
lib-link.m4 \
lib-prefix.m4 \
codeset.m4 \
gettext.m4 \
glibc21.m4 \
iconv.m4 \
intdiv0.m4 \
intmax.m4 \
inttypes.m4 \
inttypes_h.m4 \
inttypes-pri.m4 \
isc-posix.m4 \
lcmessage.m4 \
longdouble.m4 \
longlong.m4 \
nls.m4 \
po.m4 \
printf-posix.m4 \
progtest.m4 \
signed.m4 \
size_max.m4 \
stdint_h.m4 \
uintmax_t.m4 \
ulonglong.m4 \
wchar_t.m4 \
wint_t.m4 \
xsize.m4 \
"

do_stage_append() {
	for i in ${M4}; do
		src="gettext-runtime/m4/$i"
		if [ ! -f $src ]; then
			src="gettext-tools/m4/$i"
		fi
		if [ ! -f $src ]; then
			src="autoconf-lib-link/m4/$i"
		fi
		if [ ! -f $src ]; then
			echo "can't find $i" >&2
			exit 1
		fi
		install -m 0644 $src ${STAGING_DATADIR}/aclocal/$i
	done
}
