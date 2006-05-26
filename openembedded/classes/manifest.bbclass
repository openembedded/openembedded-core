
python read_manifest () {
	import sys, bb.manifest
	mfn = bb.data.getVar("MANIFEST", d, 1)
	if os.access(mfn, os.R_OK):
		# we have a manifest, so emit do_stage and do_populate_pkgs,
		# and stuff some additional bits of data into the metadata store
		mfile = file(mfn, "r")
		manifest = bb.manifest.parse(mfile, d)
		if not manifest:
			return

		bb.data.setVar('manifest', manifest, d)
}

python parse_manifest () {
		manifest = bb.data.getVar("manifest", d)
		if not manifest:
			return
		for func in ("do_populate_staging", "do_populate_pkgs"):
			value = bb.manifest.emit(func, manifest, d)
			if value:
				bb.data.setVar("manifest_" + func, value, d)
				bb.data.delVarFlag("manifest_" + func, "python", d)
				bb.data.delVarFlag("manifest_" + func, "fakeroot", d)
				bb.data.setVarFlag("manifest_" + func, "func", 1, d)
		packages = []
		for l in manifest:
			if "pkg" in l and l["pkg"] is not None:
				packages.append(l["pkg"])
		bb.data.setVar("PACKAGES", " ".join(packages), d)
}

python __anonymous () {
	try:
		bb.build.exec_func('read_manifest', d)
		bb.build.exec_func('parse_manifest', d)
	except exceptions.KeyboardInterrupt:
		raise
	except Exception, e:
		bb.error("anonymous function: %s" % e)
		pass
}

#python do_populate_staging () {
#	if not bb.data.getVar('manifest', d):
#		bb.build.exec_func('do_emit_manifest', d)
#	if bb.data.getVar('do_stage', d):
#		bb.build.exec_func('do_stage', d)
#	else:
#		bb.build.exec_func('manifest_do_populate_staging', d)
#}

#addtask populate_pkgs after do_compile
#python do_populate_pkgs () {
#	if not bb.data.getVar('manifest', d):
#		bb.build.exec_func('do_emit_manifest', d)
#	bb.build.exec_func('manifest_do_populate_pkgs', d)
#	bb.build.exec_func('package_do_shlibs', d)
#}

addtask emit_manifest
python do_emit_manifest () {
#	FIXME: emit a manifest here
#	1) adjust PATH to hit the wrapper scripts
	wrappers = bb.which(bb.data.getVar("BBPATH", d, 1), 'build/install', 0)
	path = (bb.data.getVar('PATH', d, 1) or '').split(':')
	path.insert(0, os.path.dirname(wrappers))
	bb.data.setVar('PATH', ':'.join(path), d)
#	2) exec_func("do_install", d)
	bb.build.exec_func('do_install', d)
#	3) read in data collected by the wrappers
	print("Got here2 213")
	bb.build.exec_func('read_manifest', d)
#	4) mangle the manifest we just generated, get paths back into
#	   our variable form
#	5) write it back out
#	6) re-parse it to ensure the generated functions are proper
	bb.build.exec_func('parse_manifest', d)
}
