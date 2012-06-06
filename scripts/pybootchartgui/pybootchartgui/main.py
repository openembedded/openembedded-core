import sys
import os
import optparse

import parsing
import gui
import batch

def _mk_options_parser():
	"""Make an options parser."""
	usage = "%prog [options] PATH, ..., PATH"
	version = "%prog v0.0.0"
	parser = optparse.OptionParser(usage, version=version)
	parser.add_option("-i", "--interactive", action="store_true", dest="interactive", default=False, 
			  help="start in active mode")
	parser.add_option("-f", "--format", dest="format", default = None,
			  help="image format (...); default format ...")
	parser.add_option("-o", "--output", dest="output", metavar="PATH", default=None,
			  help="output path (file or directory) where charts are stored")
	parser.add_option("-n", "--no-prune", action="store_false", dest="prune", default=True,
			  help="do not prune the process tree")
	parser.add_option("-q", "--quiet", action="store_true", dest="quiet", default=False,
			  help="suppress informational messages")
	parser.add_option("--very-quiet", action="store_true", dest="veryquiet", default=False,
			  help="suppress all messages except errors")
	parser.add_option("--verbose", action="store_true", dest="verbose", default=False,
			  help="print all messages")
	return parser

def _get_filename(paths, options):
	"""Construct a usable filename for outputs based on the paths and options given on the commandline."""
	dir = ""
	file = "bootchart"
	if options.output != None and not(os.path.isdir(options.output)):
		return options.output
	if options.output != None:
		dir = options.output
	if len(paths) == 1:
		if os.path.isdir(paths[0]):
			file = os.path.split(paths[0])[-1]
		elif os.path.splitext(paths[0])[1] in [".tar", ".tgz", ".tar.gz"]:
			file = os.path.splitext(paths[0])[0]
	return os.path.join(dir, file + "." + options.format)

def main(argv=None):
	try:
		if argv is None:
			argv = sys.argv[1:]
	
		parser = _mk_options_parser()
		options, args = parser.parse_args(argv)
	
		if len(args) == 0:
			parser.error("insufficient arguments, expected at least one path.")
			return 2

		res = parsing.parse(args, options.prune)
		if options.interactive or options.format == None:
			gui.show(res)
		else:
			filename = _get_filename(args, options)
			batch.render(res, options.format, filename)
			print "bootchart written to", filename
		return 0
	except parsing.ParseError, ex:
		print("Parse error: %s" % ex)
		return 2


if __name__ == '__main__':
	sys.exit(main())
