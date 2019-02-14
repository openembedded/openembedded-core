# test result tool - regression analysis
#
# Copyright (c) 2019, Intel Corporation.
#
# This program is free software; you can redistribute it and/or modify it
# under the terms and conditions of the GNU General Public License,
# version 2, as published by the Free Software Foundation.
#
# This program is distributed in the hope it will be useful, but WITHOUT
# ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
# FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
# more details.
#
from resulttool.resultsutils import load_json_file, get_dict_value, pop_dict_element
import json

class ResultsRegressionSelector(object):

    def get_results_unique_configurations(self, logger, results):
        unique_configurations_map = {"oeselftest": ['TEST_TYPE', 'HOST_DISTRO', 'MACHINE'],
                                     "runtime": ['TEST_TYPE', 'IMAGE_BASENAME', 'MACHINE'],
                                     "sdk": ['TEST_TYPE', 'IMAGE_BASENAME', 'MACHINE', 'SDKMACHINE'],
                                     "sdkext": ['TEST_TYPE', 'IMAGE_BASENAME', 'MACHINE', 'SDKMACHINE']}
        results_unique_configs = {}
        for k in results:
            result = results[k]
            result_configs = get_dict_value(logger, result, 'configuration')
            result_test_type = get_dict_value(logger, result_configs, 'TEST_TYPE')
            unique_configuration_keys = get_dict_value(logger, unique_configurations_map, result_test_type)
            result_unique_config = {}
            for ck in unique_configuration_keys:
                config_value = get_dict_value(logger, result_configs, ck)
                if config_value:
                    result_unique_config[ck] = config_value
            results_unique_configs[k] = result_unique_config
        return results_unique_configs

    def get_regression_base_target_pair(self, logger, base_results, target_results):
        base_configs = self.get_results_unique_configurations(logger, base_results)
        logger.debug('Retrieved base configuration: config=%s' % base_configs)
        target_configs = self.get_results_unique_configurations(logger, target_results)
        logger.debug('Retrieved target configuration: config=%s' % target_configs)
        regression_pair = {}
        for bk in base_configs:
            base_config = base_configs[bk]
            for tk in target_configs:
                target_config = target_configs[tk]
                if base_config == target_config:
                    if bk in regression_pair:
                        regression_pair[bk].append(tk)
                    else:
                        regression_pair[bk] = [tk]
        return regression_pair

    def run_regression_with_regression_pairing(self, logger, regression_pair, base_results, target_results):
        regression = ResultsRegression()
        for base in regression_pair:
            for target in regression_pair[base]:
                print('Getting regression for base=%s target=%s' % (base, target))
                regression.run(logger, base_results[base], target_results[target])

class ResultsRegression(object):

    def print_regression_result(self, result):
        if result:
            print('============================Start Regression============================')
            print('Only print regression if base status not equal target')
            print('<test case> : <base status> -> <target status>')
            print('========================================================================')
            for k in result:
                print(k, ':', result[k]['base'], '->', result[k]['target'])
            print('==============================End Regression==============================')

    def get_regression_result(self, logger, base_result, target_result):
        base_result = get_dict_value(logger, base_result, 'result')
        target_result = get_dict_value(logger, target_result, 'result')
        result = {}
        if base_result and target_result:
            logger.debug('Getting regression result')
            for k in base_result:
                base_testcase = base_result[k]
                base_status = get_dict_value(logger, base_testcase, 'status')
                if base_status:
                    target_testcase = get_dict_value(logger, target_result, k)
                    target_status = get_dict_value(logger, target_testcase, 'status')
                    if base_status != target_status:
                        result[k] = {'base': base_status, 'target': target_status}
                else:
                    logger.error('Failed to retrieved base test case status: %s' % k)
        return result

    def run(self, logger, base_result, target_result):
        if base_result and target_result:
            result = self.get_regression_result(logger, base_result, target_result)
            logger.debug('Retrieved regression result =%s' % result)
            self.print_regression_result(result)
        else:
            logger.error('Input data objects must not be empty (base_result=%s, target_result=%s)' %
                         (base_result, target_result))

def get_results_from_directory(logger, source_dir):
    from resulttool.merge import ResultsMerge
    from resulttool.resultsutils import get_directory_files
    result_files = get_directory_files(source_dir, ['.git'], 'testresults.json')
    base_results = {}
    for file in result_files:
        merge = ResultsMerge()
        results = merge.get_test_results(logger, file, '')
        base_results = merge.merge_results(base_results, results)
    return base_results

def remove_testcases_to_optimize_regression_runtime(logger, results):
    test_case_removal = ['ptestresult.rawlogs', 'ptestresult.sections']
    for r in test_case_removal:
        for k in results:
            result = get_dict_value(logger, results[k], 'result')
            pop_dict_element(logger, result, r)

def regression_file(args, logger):
    base_results = load_json_file(args.base_result_file)
    print('Successfully loaded base test results from: %s' % args.base_result_file)
    target_results = load_json_file(args.target_result_file)
    print('Successfully loaded target test results from: %s' % args.target_result_file)
    remove_testcases_to_optimize_regression_runtime(logger, base_results)
    remove_testcases_to_optimize_regression_runtime(logger, target_results)
    if args.base_result_id and args.target_result_id:
        base_result = get_dict_value(logger, base_results, base_result_id)
        print('Getting base test result with result_id=%s' % base_result_id)
        target_result = get_dict_value(logger, target_results, target_result_id)
        print('Getting target test result with result_id=%s' % target_result_id)
        regression = ResultsRegression()
        regression.run(logger, base_result, target_result)
    else:
        regression = ResultsRegressionSelector()
        regression_pair = regression.get_regression_base_target_pair(logger, base_results, target_results)
        logger.debug('Retrieved regression pair=%s' % regression_pair)
        regression.run_regression_with_regression_pairing(logger, regression_pair, base_results, target_results)
    return 0

def regression_directory(args, logger):
    base_results = get_results_from_directory(logger, args.base_result_directory)
    target_results = get_results_from_directory(logger, args.target_result_directory)
    remove_testcases_to_optimize_regression_runtime(logger, base_results)
    remove_testcases_to_optimize_regression_runtime(logger, target_results)
    regression = ResultsRegressionSelector()
    regression_pair = regression.get_regression_base_target_pair(logger, base_results, target_results)
    logger.debug('Retrieved regression pair=%s' % regression_pair)
    regression.run_regression_with_regression_pairing(logger, regression_pair, base_results, target_results)
    return 0

def regression_git(args, logger):
    from resulttool.resultsutils import checkout_git_dir
    base_results = {}
    target_results = {}
    if checkout_git_dir(args.source_dir, args.base_git_branch):
        base_results = get_results_from_directory(logger, args.source_dir)
    if checkout_git_dir(args.source_dir, args.target_git_branch):
        target_results = get_results_from_directory(logger, args.source_dir)
    if base_results and target_results:
        remove_testcases_to_optimize_regression_runtime(logger, base_results)
        remove_testcases_to_optimize_regression_runtime(logger, target_results)
        regression = ResultsRegressionSelector()
        regression_pair = regression.get_regression_base_target_pair(logger, base_results, target_results)
        logger.debug('Retrieved regression pair=%s' % regression_pair)
        regression.run_regression_with_regression_pairing(logger, regression_pair, base_results, target_results)
    return 0

def register_commands(subparsers):
    """Register subcommands from this plugin"""
    parser_build = subparsers.add_parser('regression-file', help='regression file analysis',
                                         description='regression analysis comparing base result set to target '
                                                     'result set',
                                         group='analysis')
    parser_build.set_defaults(func=regression_file)
    parser_build.add_argument('base_result_file',
                              help='base result file provide the base result set')
    parser_build.add_argument('target_result_file',
                              help='target result file provide the target result set for comparison with base result')
    parser_build.add_argument('-b', '--base-result-id', default='',
                              help='(optional) default select regression based on configurations unless base result '
                                   'id was provided')
    parser_build.add_argument('-t', '--target-result-id', default='',
                              help='(optional) default select regression based on configurations unless target result '
                                   'id was provided')

    parser_build = subparsers.add_parser('regression-dir', help='regression directory analysis',
                                         description='regression analysis comparing base result set to target '
                                                     'result set',
                                         group='analysis')
    parser_build.set_defaults(func=regression_directory)
    parser_build.add_argument('base_result_directory',
                              help='base result directory provide the files for base result set')
    parser_build.add_argument('target_result_directory',
                              help='target result file provide the files for target result set for comparison with '
                                   'base result')

    parser_build = subparsers.add_parser('regression-git', help='regression git analysis',
                                         description='regression analysis comparing base result set to target '
                                                     'result set',
                                         group='analysis')
    parser_build.set_defaults(func=regression_git)
    parser_build.add_argument('source_dir',
                              help='source directory that contain the git repository with test result files')
    parser_build.add_argument('base_git_branch',
                              help='base git branch that provide the files for base result set')
    parser_build.add_argument('target_git_branch',
                              help='target git branch that provide the files for target result set for comparison with '
                                   'base result')
