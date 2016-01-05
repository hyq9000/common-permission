/*
 * 作用于：所有页面,
 * 实现省市下拉联动效果；
 * 顺序依赖文件;
 * 作者：hyq
 * 创建时间：2012-7-8
 */
 
//定义以省名为key，市名数组为值的二维数组；
cities = new Object();
cities['安徽'] = new Array('合肥', '芜湖', '蚌埠', '淮南', '马鞍山', '淮北', '铜陵', '安庆', '黄山',
		'滁州', '阜阳', '宿州', '巢湖', '六安', '亳州', '池州', '宣城');
cities['北京'] = new Array('东城区', '西城区', '崇文区', '宣武区', '朝阳区', '丰台区', '石景山区',
		'海淀区', '门头沟区', '房山区', '通州区', '顺义区', '昌平区', '大兴区', '怀柔区', '平谷区', '密云县',
		'延庆县');
cities['重庆'] = new Array('万州区', '涪陵区', '渝中区', '大渡口区', '江北区', '沙坪坝区', '九龙坡区',
		'南岸区', '北碚区', '万盛区', '双桥区', '渝北区', '巴南区', '黔江区', '长寿区', '綦江县', '潼南县',
		'铜梁县', '大足县', '荣昌县', '璧山县', '梁平县', '城口县', '丰都县', '垫江县', '武隆县', '忠县',
		'开县', '云阳县', '奉节县', '巫山县', '巫溪县', '石柱土家族自治县', '秀山土家族苗族自治县',
		'酉阳土家族苗族自治县', '彭水苗族土家族自治县', '江津区', '合川区', '永川区', '南川区');
cities['福建'] = new Array('福州', '厦门', '莆田', '三明', '泉州', '漳州', '南平', '龙岩', '宁德');
cities['甘肃'] = new Array('兰州', '嘉峪关', '金昌', '白银', '天水', '武威', '张掖', '平凉', '酒泉','庆阳', '定西', '陇南', '临夏', '甘南');
cities['广东'] = new Array('广州', '韶关', '深圳', '珠海', '汕头', '佛山', '江门', '湛江', '茂名',
		'肇庆', '惠州', '梅州', '汕尾', '河源', '阳江', '清远', '东莞', '中山', '潮州', '揭阳', '云浮');
cities['广西'] = new Array('南宁', '柳州', '桂林', '梧州', '北海', '防城港', '钦州', '贵港', '玉林','百色', '贺州', '河池', '来宾', '崇左');

cities['贵州'] = new Array('贵阳', '六盘水', '遵义', '安顺', '铜仁', '黔西南', '毕节', '黔东南','黔南','临高','昌江');
cities['海南'] = new Array('海口', '三亚', '其他','五指山','琼海','儋州','文昌','万宁','东方','定安','屯昌','澄迈','白沙','乐东','陵水','保亭','琼中');
cities['河北'] = new Array('石家庄', '唐山', '秦皇岛', '邯郸', '邢台', '保定', '张家口', '承德','沧州', '廊坊', '衡水');
cities['黑龙江'] = new Array('哈尔滨', '齐齐哈尔', '鸡西', '鹤岗', '双鸭山', '大庆', '伊春', '佳木斯',	'七台河', '牡丹江', '黑河', '绥化', '大兴安岭');
cities['河南'] = new Array('郑州', '开封', '洛阳', '平顶山', '安阳', '鹤壁', '新乡', '焦作', '濮阳','许昌', '漯河', '三门峡', '南阳', '商丘', '信阳', '周口', '驻马店', '济源');
cities['湖北'] = new Array('武汉', '黄石', '十堰', '宜昌', '襄阳', '鄂州', '荆门', '孝感', '荆州',
		'黄冈', '咸宁', '随州', '恩施土家族苗族自治州', '仙桃', '潜江', '天门', '神农架');
cities['湖南'] = new Array('长沙', '株洲', '湘潭', '衡阳', '邵阳', '岳阳', '常德', '张家界', '益阳',
		'郴州', '永州', '怀化', '娄底', '湘西土家族苗族自治州');
cities['内蒙古'] = new Array('呼和浩特', '包头', '乌海', '赤峰', '通辽', '鄂尔多斯', '呼伦贝尔',
		'兴安盟', '锡林郭勒盟', '乌兰察布盟', '巴彦淖尔盟', '阿拉善盟');
cities['江苏'] = new Array('南京', '无锡', '徐州', '常州', '苏州', '南通', '连云港', '淮安', '盐城',
		'扬州', '镇江', '泰州', '宿迁');
cities['江西'] = new Array('南昌', '景德镇', '萍乡', '九江', '新余', '鹰潭', '赣州', '吉安', '宜春',
		'抚州', '上饶');
cities['吉林'] = new Array('长春', '吉林', '四平', '辽源', '通化', '白山', '松原', '白城',
		'延边朝鲜族自治州');
cities['辽宁'] = new Array('沈阳', '大连', '鞍山', '抚顺', '本溪', '丹东', '锦州', '营口', '阜新',
		'辽阳', '盘锦', '铁岭', '朝阳', '葫芦岛');
cities['宁夏'] = new Array('银川', '石嘴山', '吴忠', '固原', '中卫');
cities['青海'] = new Array('西宁', '海东', '海北', '黄南', '海南', '果洛', '玉树', '海西');
cities['山西'] = new Array('太原', '大同', '阳泉', '长治', '晋城', '朔州', '晋中', '运城', '忻州',	'临汾', '吕梁');
cities['山东'] = new Array('济南', '青岛', '淄博', '枣庄', '东营', '烟台', '潍坊', '济宁', '泰安',	'威海', '日照', '莱芜', '临沂', '德州', '聊城', '滨州', '菏泽');
cities['上海'] = new Array('黄浦区', '卢湾区', '徐汇区', '长宁区', '静安区', '普陀区', '闸北区',
		'虹口区', '杨浦区', '闵行区', '宝山区', '嘉定区', '浦东新区', '金山区', '松江区', '青浦区', '南汇区',
		'奉贤区', '崇明县');
cities['四川'] = new Array('成都', '自贡', '攀枝花', '泸州', '德阳', '绵阳', '广元', '遂宁', '内江',
		'乐山', '南充', '眉山', '宜宾', '广安', '达州', '雅安', '巴中', '资阳', '阿坝', '甘孜', '凉山');
cities['天津'] = new Array('和平区', '河东区', '河西区', '南开区', '河北区', '红桥区', '塘沽区',
		'汉沽区', '大港区', '东丽区', '西青区', '津南区', '北辰区', '武清区', '宝坻区', '宁河县', '静海县',
		'蓟县', '滨海新区', '保税区');
cities['西藏'] = new Array('拉萨', '昌都', '山南', '日喀则', '那曲', '阿里', '林芝');
cities['新疆'] = new Array('乌鲁木齐', '克拉玛依', '吐鲁番', '哈密', '昌吉', '博尔塔拉', '巴音郭楞',
		'阿克苏', '克孜勒苏', '喀什', '和田', '伊犁', '塔城', '阿勒泰', '石河子');
cities['云南'] = new Array('昆明', '曲靖', '玉溪', '保山', '昭通', '楚雄', '红河', '文山', '思茅',
		'西双版纳', '大理', '德宏', '丽江', '怒江', '迪庆', '临沧');
cities['浙江'] = new Array('杭州', '宁波', '温州', '嘉兴', '湖州', '绍兴', '金华', '衢州', '舟山',
		'台州', '丽水');
cities['陕西'] = new Array('西安', '铜川', '宝鸡', '咸阳', '渭南', '延安', '汉中', '榆林', '安康',
		'商洛');
cities['台湾'] = new Array('台北市', '高雄市', '基隆市', '台中市', '台南市', '新竹市', '嘉义市',
		'台北县', '宜兰县', '桃园县', '新竹县', '苗栗县', '台中县', '彰化县', '南投县', '云林县', '嘉义县',
		'台南县', '高雄县', '屏东县', '澎湖县', '台东县', '花莲县', '其他');
cities['香港'] = new Array('中西区', '东区', '九龙城区', '观塘区', '南区', '深水埗区', '黄大仙区',
		'湾仔区', '油尖旺区', '离岛区', '葵青区', '北区', '西贡区', '沙田区', '屯门区', '大埔区', '荃湾区',
		'元朗区', '其他');
cities['澳门'] = new Array('花地玛堂区', '圣安多尼堂区', '大堂区', '望德堂区', '风顺堂区', '氹仔', '路环',
		'其他');
cities['海外'] = new Array('美国', '英国', '法国', '俄罗斯', '加拿大', '巴西', '澳大利亚', '印尼',
		'泰国', '马来西亚', '新加坡', '菲律宾', '越南', '印度', '日本', '新西兰', '韩国', '德国', '意大利',
		'爱尔兰', '荷兰', '瑞士', '乌克兰', '南非', '芬兰', '瑞典', '奥地利', '西班牙', '比利时', '挪威',
		'丹麦', '波兰', '阿根廷', '白俄罗斯', '哥伦比亚', '古巴', '埃及', '希腊', '匈牙利', '伊朗', '蒙古',
		'墨西哥', '葡萄牙', '沙特阿拉伯', '土耳其', '捷克', '斯洛伐克', '其他');

/*
* 当省级下拉组件的onchange发生时，动态的将对应的市名显示到市级下接里边；
* 代码是从网上直接COPY的；
*/
function set_city(province, provincevalue, city, cityvalue){
	var pv, cv;
	var i, ii;
	pv = provincevalue;
	cv = cityvalue;
	//当省级下拉选择不正确时，清理市级下拉选项；
	if (pv == '0')
		return;
	if (typeof (cities[pv]) == 'undefined'){
		city.length = 1;
		return;
	}else{
		city.length = 0;
	}
	//将选中的省级所属所有市名加入市级下拉中；
	for (i = 0; i < cities[pv].length; i++){
		ii = i;
		city.options[ii] = new Option();
		city.options[ii].text = cities[pv][i];
		city.options[ii].value = cities[pv][i];
	}
	//设置市级下拉的第一个选项为默认选中；
	city.selectedIndex=0;
}


