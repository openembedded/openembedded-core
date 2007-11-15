do_install_append() {
        for i in `find ${D} -name "*.py"` ; do \
            sed -i -e s:${D}::g $i
        done

        for i in `find ${D} -name "*.la"` ; do \
            sed -i -e s:${STAGING_LIBDIR}:${libdir}:g $i
        done
        
        if test -e ${D}${bindir} ; then
            for i in ${D}${bindir}/* ; do \
                sed -i -e s:${STAGING_BINDIR_NATIVE}:${bindir}:g $i
            done
        fi

        if test -e ${D}${sbindir} ; then
            for i in ${D}${sbindir}/* ; do \
                sed -i -e s:${STAGING_BINDIR_NATIVE}:${bindir}:g $i
            done
        fi
}

