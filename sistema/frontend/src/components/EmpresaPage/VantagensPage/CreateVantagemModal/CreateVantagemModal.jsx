import { useState } from "react";
import axios from "axios";
import {
	FormGroup,
	FormControl,
	InputLabel,
	Input,
	Button,
	Box,
	Typography,
	styled
} from "@mui/material";
import { useForm } from "react-hook-form";
import CloudUploadIcon from "@mui/icons-material/CloudUpload";

export default function CreateVantagemModal({ handleClose, setEmpresa }) {
	const { register, handleSubmit, formState: { errors } } = useForm();
	const [cloathImage, setImage] = useState("");
	const [showImg, setShowImg] = useState();

	const handleClick = (e) => {
		submit(e);
	};

	function atualizaEmpresa() {
		let empresaId = localStorage.getItem("idUser");
		fetch(`http://localhost:8080/empresas/${empresaId}`)
			.then((res) => res.json())
			.then((res) => setEmpresa(res));
	}

	function submit({ descricao, custo, quantidade, foto }) {
		// Validação adicional, embora react-hook-form deva pegar
		if (parseFloat(custo) < 0 || parseInt(quantidade) < 0) {
			alert("Custo e Quantidade não podem ser negativos.");
			return;
		}

		// TODO fazer requisição post
		var bodyFormData = new FormData();
		bodyFormData.append("descricao", descricao);
		bodyFormData.append("custoEmMoedas", custo);
		bodyFormData.append("quantidade", quantidade);
		bodyFormData.append("foto", foto[0] || "");
		try {
			let empresaId = localStorage.getItem("idUser");
			axios({
				method: "post",
				url: `http://localhost:8080/empresas/${empresaId}/vantagens`,
				data: bodyFormData,
				headers: {
					"Content-Type": `multipart/form-data;`,
				},
			}).then((res) => {
				if (res.status === 201) {
					alert("Vantagem criada!");
					atualizaEmpresa();
					handleClose();
				} else {
					const errorMessage = res.data?.message || "Não foi possível criar a vantagem!";
					alert(errorMessage);
				}
			}).catch(err => {
				const errorMessage = err.response?.data?.message || err.response?.data || "Erro ao criar vantagem. Verifique os dados.";
				alert(errorMessage);
			});
		} catch (error) {
			console.error(error.response.data);
		}
	}

	const labels = [
		{ 
			id: "descricao", 
			label: "Descrição", 
			type: "text", 
			rules: { required: "Descrição é obrigatória" } 
		},
		{
			id: "custo",
			label: "Custo em Moedas",
			type: "number",
			rules: {
				required: "Custo é obrigatório",
				min: { value: 0, message: "Custo não pode ser negativo" },
			},
		},
		{
			id: "quantidade",
			label: "Quantidade em Estoque",
			type: "number",
			rules: { required: "Quantidade é obrigatória", min: { value: 0, message: "Quantidade não pode ser negativa" } },
		},
	];

	const handleImageChange = (e) => {
		const file = e.target.files[0];
		setImage(file);
		setShowImg(file);
		handleOpenImg();
	};

	const VisuallyHiddenInput = styled("input")({
		clip: "rect(0 0 0 0)",
		clipPath: "inset(50%)",
		height: 1,
		overflow: "hidden",
		position: "absolute",
		bottom: 0,
		left: 0,
		whiteSpace: "nowrap",
		width: 1,
	});
	const ButtonStyle = {
		variant: "contained",
		disableRipple: true,
		size: "large",
		fullWidth: true,
	};
	return (
		<>
			<div className='form-container'>
				<Box>
					<FormGroup>
						{labels.map((field) => (
							<FormControl key={field.id} className='field-container' sx={{ my: 1 }}>
								<InputLabel htmlFor={field.id}>{field.label}</InputLabel>
								<Input
									type={field.type}
									id={field.id}
									name={field.id}
									{...register(field.id, field.rules)}
									// Adiciona props de erro para o Input do MUI
									error={!!errors[field.id]}
								/>
								{errors[field.id] && (
									<Typography variant="caption" color="error" sx={{ mt: 0.5 }}>
										{errors[field.id].message}
									</Typography>
								)}								
							</FormControl>
						))}
						<Button
							{...ButtonStyle}
							component='label'
							onChange={handleImageChange}
							startIcon={<CloudUploadIcon />}
						>
							{cloathImage.name ? "Alterar Foto" : "Adicione uma Foto"}
							<VisuallyHiddenInput
								type='file'
								name='imagem'
								accept='image/*'
								{...register("foto")}
							/>
						</Button>
					</FormGroup>
					<Button
						{...ButtonStyle}
						variant='contained'
						onClick={handleSubmit(handleClick)}
						color='secondary'
					>
						Criar Vantagem
					</Button>
				</Box>
			</div>
		</>
	);
}
